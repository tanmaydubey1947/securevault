package com.securevault.util;

import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.user.User;
import com.securevault.service.auth.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public enum Util {

    INSTANCE;

    public String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            return user.getEmail();
        } else if (principal instanceof AuthUserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new RuntimeException("Invalid authentication principal");
    }

    public TransactionResponse transactionMapper(final Transaction transaction) {
        final TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setSender(transaction.getSender());
        response.setReceiver(transaction.getReceiver());
        response.setAmount(transaction.getAmount());
        response.setTransactionType(transaction.getTransactionType().name());
        response.setTransactionStatus(transaction.getTransactionStatus().name());
        response.setBankReference(transaction.getBankReference());
        response.setIdempotencyKey(transaction.getIdempotencyKey());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
