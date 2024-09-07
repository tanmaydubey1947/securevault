package com.securevault.paymentservice.service;

import com.securevault.paymentservice.dao.PaymentDAO;
import com.securevault.paymentservice.entity.UserInfo;
import com.securevault.userservice.entity.AddMoney;
import com.securevault.userservice.entity.SendMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    public void addMoney(AddMoney addMoney) {
        UserInfo currentData = paymentDAO.getUserInfo(addMoney.getAccountNumber());
        double depositAmount = addMoney.getAmount();
        double existingBalance = currentData.getAccountBalance();
        double newBalance = depositAmount + existingBalance;
        currentData.setAccountBalance(newBalance);
        paymentDAO.updateUserInfo(currentData);
    }

    public void sendMoney(SendMoney sendMoney) {
        UserInfo fromUser = paymentDAO.getUserInfo(sendMoney.getFromAccountNumber());
        UserInfo toUser = paymentDAO.getUserInfo(sendMoney.getToAccountNumber());

        double depositAmount = sendMoney.getAmount();
        double existingToUserBalance = toUser.getAccountBalance();
        double existingFromUserBalance = fromUser.getAccountBalance();

        double newBalanceToUser = existingToUserBalance + depositAmount;
        toUser.setAccountBalance(newBalanceToUser);

        double newBalanceFromUser = existingFromUserBalance - depositAmount;
        fromUser.setAccountBalance(newBalanceFromUser);

        paymentDAO.updateUserInfo(toUser);
        paymentDAO.updateUserInfo(fromUser);
    }


}
