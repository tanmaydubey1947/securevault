package com.securevault.exception.custom;

public class ConcurrentWalletUpdateException extends Exception {

    public ConcurrentWalletUpdateException(String message) {
        super(message);
    }
}
