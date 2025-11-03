package com.securevault.util.rowMapper;

import com.securevault.model.entity.Wallet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class WalletRowMapper implements RowMapper<Wallet> {

    @Override
    public Wallet mapRow(ResultSet rs, int rowNum) {
        final Wallet wallet = new Wallet();
        try {
            wallet.setId(rs.getInt("id"));
            wallet.setUserEmail(rs.getString("user_email"));
            wallet.setAvailableAmount(rs.getDouble("available_amount"));
            wallet.setPendingAmount(rs.getDouble("pending_amount"));
            wallet.setVersion(rs.getInt("version"));
            wallet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        } catch (final Exception e) {
            throw new RuntimeException("Error mapping row to Wallet object", e);
        }
        return wallet;
    }
}
