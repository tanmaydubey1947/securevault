package com.securevault.util.rowMapper;

import com.securevault.entity.user.AccountStatus;
import com.securevault.entity.user.KycStatus;
import com.securevault.entity.user.Role;
import com.securevault.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;

@Slf4j
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
        } catch (Exception e) {
            log.error("Error mapping row to User object", e);
        }
        return user;
    }
}
