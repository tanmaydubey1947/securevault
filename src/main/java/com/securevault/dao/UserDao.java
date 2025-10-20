package com.securevault.dao;

import com.securevault.entity.user.User;
import com.securevault.util.rowMapper.UsersRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDao {

    @Autowired private JdbcTemplate jdbcTemplate;

    public User getUserByEmail(final String email) {
        final String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UsersRowMapper(), email);
    }

}
