package com.securevault.exception.custom;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException() {
        super();
    }

    public WalletNotFoundException(String message) {
        super(message);
    }
}
