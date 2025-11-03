package com.securevault.util.rowMapper;

import com.securevault.model.entity.user.AccountStatus;
import com.securevault.model.entity.user.KycStatus;
import com.securevault.model.entity.user.Role;
import com.securevault.model.entity.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class UsersRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) {
        final User user = new User();
        try {
            user.setId(rs.getInt("id"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password_hash"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setKycStatus(KycStatus.fromString(rs.getString("kyc_status")));
            user.setAccountStatus(AccountStatus.fromString(rs.getString("account_status")));
            user.setRole(Role.fromString(rs.getString("role")));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        } catch (final Exception e) {
            throw new RuntimeException("Error mapping row to User object", e);
        }
        return user;
    }
}
