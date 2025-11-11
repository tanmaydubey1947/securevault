package com.securevault.service.transaction;

import com.securevault.dao.TransactionDao;
import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.entity.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.securevault.model.entity.transaction.TransactionType.TRANSFER;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao dao;

    @Override
    public TransactionResponse processTransaction(final TransactionRequest transactionRequest) {
        final Transaction transaction = new Transaction();
        transaction.setSender(transactionRequest.getSenderMail());
        transaction.setReceiver(transactionRequest.getReceiverMail());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionType(TRANSFER);
        transaction.setBankReference(transactionRequest.getDescription());
        transaction.setIdempotencyKey(UUID.randomUUID().toString());
        try {
            int trxId = dao.transferToWallet(transaction);
            final TransactionResponse response = new TransactionResponse();
            response.setTransactionId(trxId);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
