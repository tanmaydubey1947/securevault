package com.securevault.service.transaction;

import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse sendToWallet(TransactionRequest transactionRequest);

    TransactionResponse sendToBank(TransactionRequest transactionRequest);

    TransactionResponse addToWallet(TransactionRequest transactionRequest);

    List<TransactionResponse> getAllTransactions();
}
