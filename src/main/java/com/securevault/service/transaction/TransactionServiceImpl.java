package com.securevault.service.transaction;

import com.securevault.dao.TransactionDao;
import com.securevault.exception.custom.InsufficientBalanceException;
import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.entity.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.securevault.model.entity.transaction.TransactionType.TRANSFER;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao dao;

    @Override
    @Transactional
    public TransactionResponse sendToWallet(final TransactionRequest request) {

        if(request.getAmount() <= 0){
            throw new InsufficientBalanceException("Transfer amount must be greater than zero");
        }

        final Transaction transaction = new Transaction();
        transaction.setSender(request.getSenderMail());
        transaction.setReceiver(request.getReceiverMail());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TRANSFER);
        transaction.setBankReference(request.getDescription());
        transaction.setIdempotencyKey(UUID.randomUUID().toString());
        int trxId = dao.sendToWallet(transaction);
        final TransactionResponse response = new TransactionResponse();
        response.setTransactionId(trxId);
        return response;
    }
}
