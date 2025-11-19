package com.securevault.service.transaction;

import com.securevault.dao.TransactionDao;
import com.securevault.exception.custom.InsufficientBalanceException;
import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        if (request.getAmount() <= 0) {
            throw new InsufficientBalanceException("Transfer amount must be greater than zero");
        }

        final Transaction transaction = new Transaction();
        transaction.setSender(Util.INSTANCE.getCurrentEmail());
        transaction.setReceiver(request.getReceiverMail());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TRANSFER);
        transaction.setBankReference("Send To Wallet");
        transaction.setIdempotencyKey(UUID.randomUUID().toString());
        int trxId = dao.sendToWallet(transaction);
        final TransactionResponse response = new TransactionResponse();
        response.setTransactionId(trxId);
        return response;
    }

    @Override
    public TransactionResponse sendToBank(final TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public TransactionResponse addToWallet(final TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public List<TransactionResponse> getAllTransactions() { //TODO: Implement pagination and filtering with mapper
        final List<Transaction> transactions = dao.getAllTransactions();
        final List<TransactionResponse> response = transactions.stream()
                .map(Util.INSTANCE::transactionMapper)
                .toList();
        return response;
    }
}
