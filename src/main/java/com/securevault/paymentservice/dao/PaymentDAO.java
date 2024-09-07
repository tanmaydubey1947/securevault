package com.securevault.paymentservice.dao;

import com.securevault.paymentservice.entity.UserInfo;

public interface PaymentDAO {

    int updateUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(int accountNumber);

}
