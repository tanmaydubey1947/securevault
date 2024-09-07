package com.securevault.paymentservice.dao.impl;

import com.securevault.paymentservice.dao.PaymentDAO;
import com.securevault.paymentservice.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class PaymentDAOImpl implements PaymentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int updateUserInfo(UserInfo userInfo) {
        String query = "UPDATE USER_INFO SET ACCOUNT_BALANCE = ? WHERE ACCOUNT_NUMBER = ?";
        return jdbcTemplate.update(query, userInfo.getAccountBalance(), userInfo.getAccountNumber());
    }

    @Override
    public UserInfo getUserInfo(int accountNumber) {
        String query = "SELECT * FROM USER_INFO WHERE ACCOUNT_NUMBER = ?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(UserInfo.class), accountNumber);
    }


}
