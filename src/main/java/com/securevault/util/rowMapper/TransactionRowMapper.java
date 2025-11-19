package com.securevault.util.rowMapper;

import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.transaction.TransactionStatus;
import com.securevault.model.entity.transaction.TransactionType;
import com.securevault.model.entity.user.AccountStatus;
import com.securevault.model.entity.user.KycStatus;
import com.securevault.model.entity.user.Role;
import com.securevault.model.entity.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) {
        final Transaction transaction = new Transaction();
        try {
            transaction.setTransactionId(rs.getInt("id"));
            transaction.setSender(rs.getString("sender"));
            transaction.setReceiver(rs.getString("receiver"));
            transaction.setAmount(rs.getDouble("amount"));
            transaction.setTransactionType(TransactionType.fromString(rs.getString("type")));
            transaction.setTransactionStatus(TransactionStatus.fromString(rs.getString("status")));
            transaction.setBankReference(rs.getString("bank_ref"));
            transaction.setIdempotencyKey(rs.getString("idempotency_key"));
            transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        } catch (final Exception e) {
            throw new RuntimeException("Error mapping row to Transaction object", e);
        }
        return transaction;
    }
}
