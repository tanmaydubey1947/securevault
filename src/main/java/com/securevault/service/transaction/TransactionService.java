package com.securevault.service.transaction;

import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;

public interface TransactionService {

    TransactionResponse sendToWallet(TransactionRequest transactionRequest);


}
