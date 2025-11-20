package com.securevault.dao;

import com.securevault.exception.custom.ConcurrentWalletUpdateException;
import com.securevault.exception.custom.InsufficientBalanceException;
import com.securevault.exception.custom.WalletNotFoundException;
import com.securevault.model.entity.Wallet;
import com.securevault.model.entity.transaction.IdempotencyKey;
import com.securevault.model.entity.transaction.LedgerEntry;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.transaction.TransactionStatus;
import com.securevault.util.rowMapper.TransactionRowMapper;
import com.securevault.util.rowMapper.WalletRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.securevault.model.entity.transaction.TransactionStatus.SUCCESS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
@Slf4j
public class TransactionDao {

    @Autowired private JdbcTemplate jdbcTemplate;

    private static final String SELECT_WALLET_SQL =
            "SELECT * FROM wallets WHERE user_email = ?";
    private static final String UPDATE_WALLET_SQL =
            "UPDATE wallets SET available_amount = available_amount + ?, version = version + 1 " +
                    "WHERE user_email = ? AND version = ?";
    private static final String INSERT_TRANSACTION_SQL = """
            INSERT INTO transactions (
                sender, type, status, amount, receiver, bank_ref, idempotency_key, created_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public int sendToWallet(final Transaction transaction) throws RuntimeException {

        final String sender = transaction.getSender();
        final String receiver = transaction.getReceiver();
        final double trxAmount = transaction.getAmount();

        final Wallet senderWallet = getWallet(sender);
        final Wallet receiverWallet = getWallet(receiver);

        if (senderWallet.getAvailableAmount() < trxAmount) {
            throw new InsufficientBalanceException("Insufficient balance in sender's wallet");
        }

        int senderUpdated = jdbcTemplate.update(
                UPDATE_WALLET_SQL, -trxAmount, sender, senderWallet.getVersion());
        if (senderUpdated == 0) {
            throw new ConcurrentWalletUpdateException("Concurrent modification on Sender's Wallet");
        }

        int receiverUpdated = jdbcTemplate.update(
                UPDATE_WALLET_SQL, trxAmount, receiver, receiverWallet.getVersion());
        if (receiverUpdated == 0) {
            throw new ConcurrentWalletUpdateException("Concurrent modification on Receiver's Wallet");
        }

        transaction.setTransactionStatus(SUCCESS);
        int txnId = logTransactions(transaction);
        log.info("Transfer successful: sender={}, receiver={}, amount={}, txnId={}",
                sender, receiver, trxAmount, txnId);

        return txnId;
    }

    public List<Transaction> getAllTransactions() {
        final String query = "SELECT * FROM transactions";
        return jdbcTemplate.query(query, new TransactionRowMapper());
    }

    private Wallet getWallet(final String userEmail) throws WalletNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SELECT_WALLET_SQL, new WalletRowMapper(), userEmail);
        } catch (EmptyResultDataAccessException e) {
            throw new WalletNotFoundException("Wallet not found for user: " + userEmail);
        }
    }

    private int logTransactions(final Transaction transaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_TRANSACTION_SQL, Statement.RETURN_GENERATED_KEYS);
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

        return keyHolder.getKey().intValue();
    }

    public void logTransactionWithLedgerAndIdempotency(final Transaction transaction,
                                                       final LedgerEntry debitEntry,
                                                       final LedgerEntry creditEntry,
                                                       final IdempotencyKey idempotency) { //TODO: Check in future how to handle this

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

    public int adjustWalletAmount(final Transaction transaction) {
        final String userEmail = transaction.getReceiver();
        final double amount = transaction.getAmount();

        final Wallet wallet = getWallet(userEmail);

        int updated = jdbcTemplate.update(
                UPDATE_WALLET_SQL, amount, userEmail, wallet.getVersion());
        if (updated == 0) {
            throw new ConcurrentWalletUpdateException("Concurrent modification on Wallet during adjustment");
        }

        transaction.setTransactionStatus(SUCCESS);
        int txnId = logTransactions(transaction);
        log.info("Adjustment successful: user={}, amount={}, txnId={}",
                userEmail, amount, txnId);

        return txnId;
    }

}
