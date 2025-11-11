package com.securevault.service.transaction;

import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;

public interface TransactionService {

    TransactionResponse processTransaction(TransactionRequest transactionRequest);


}
