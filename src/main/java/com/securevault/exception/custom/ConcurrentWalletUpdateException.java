package com.securevault.exception.custom;

public class ConcurrentWalletUpdateException extends RuntimeException {

    ConcurrentWalletUpdateException() {
        super();
    }
    public ConcurrentWalletUpdateException(String message) {
        super(message);
    }
}
