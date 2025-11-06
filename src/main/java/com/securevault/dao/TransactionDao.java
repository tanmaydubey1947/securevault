package com.securevault.dao;

import com.securevault.model.entity.transaction.IdempotencyKey;
import com.securevault.model.entity.transaction.LedgerEntry;
import com.securevault.model.entity.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TransactionDao {

    @Autowired private JdbcTemplate jdbcTemplate;

    public void logTransactionWithLedgerAndIdempotency(final Transaction transaction,
                                                       final LedgerEntry debitEntry,
                                                       final LedgerEntry creditEntry,
                                                       final IdempotencyKey idempotency) {

        final String txnSql = """
                    INSERT INTO transactions (
                        sender, type, status, amount, receiver, bank_ref, idempotency_key, created_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(txnSql, RETURN_GENERATED_KEYS);
            ps.setString(1, transaction.getSender());
            ps.setString(2, transaction.getTransactionType().name());
            ps.setString(3, transaction.getTransactionStatus().name());
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getReceiver());
            ps.setString(6, transaction.getBankReference());
            ps.setString(7, transaction.getIdempotencyKey());
            ps.setTimestamp(8, Timestamp.from(Instant.now()));
            return ps;
        }, keyHolder);

        final int transactionId = keyHolder.getKey().intValue();
        transaction.setTransactionId(transactionId);

        final String ledgerSql = """
                    INSERT INTO ledger_entries (
                        transaction_id, account_ref, entry_type, amount, created_at
                    ) VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(ledgerSql,
                transactionId,
                debitEntry.getEmail(),
                debitEntry.getEntryType().name(),
                debitEntry.getAmount(),
                Timestamp.from(Instant.now())
        );

        jdbcTemplate.update(ledgerSql,
                transactionId,
                creditEntry.getEmail(),
                creditEntry.getEntryType().name(),
                creditEntry.getAmount(),
                Timestamp.from(Instant.now())
        );

        final String idempotencySql = """
                    INSERT INTO idempotency_keys (
                        idempotency_key, user_email, transaction_id, created_at
                    ) VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(idempotencySql,
                idempotency.getIdempotencyKey(),
                idempotency.getUserEmail(),
                transactionId,
                Timestamp.from(Instant.now())
        );
    }


}
