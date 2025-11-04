package com.securevault.exception.custom;

public class WalletNotFound extends RuntimeException {

    public WalletNotFound() {
        super();
    }

    public WalletNotFound(final String message) {
        super(message);
    }

}
