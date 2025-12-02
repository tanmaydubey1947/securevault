package com.securevault.util;

import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.model.dto.user.UserResponse;
import com.securevault.model.entity.Wallet;
import com.securevault.model.entity.transaction.Transaction;
import com.securevault.model.entity.user.User;
import com.securevault.service.auth.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

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

    public List<TransactionResponse> mergeAndSortTransactions(final List<Transaction> creditTransactions,
                                                              final List<Transaction> debitTransactions) {
        final List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(creditTransactions);
        allTransactions.addAll(debitTransactions);
        allTransactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return allTransactions.stream()
                .map(this::transactionMapper)
                .toList();
    }

    public List<UserResponse> mergeWalletWithUser(final List<User> users, final List<Wallet> wallets) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setFullName(user.getFullName());
            userResponse.setEmail(user.getEmail());
            userResponse.setPhoneNumber(user.getPhoneNumber());
            userResponse.setKycStatus(user.getKycStatus().name());
            userResponse.setAccountStatus(user.getAccountStatus().name());
            userResponse.setRole(user.getRole().name());
            userResponse.setCreatedAt(user.getCreatedAt());

            for (Wallet wallet : wallets) {
                if (wallet.getUserEmail().equals(user.getEmail())) {
                    userResponse.setAvailableAmount(wallet.getAvailableAmount());
                    userResponse.setPendingAmount(wallet.getPendingAmount());
                    userResponse.setWalletVersion(wallet.getVersion());
                    break;
                }
            }
            userResponses.add(userResponse);
        }
        return userResponses;
    }
}
