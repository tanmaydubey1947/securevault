package com.securevault.service.transaction;

import com.securevault.model.dto.transaction.TransactionRequest;

public interface TransactionService {

    TransactionRequest processTransaction(TransactionRequest transactionRequest);


}
