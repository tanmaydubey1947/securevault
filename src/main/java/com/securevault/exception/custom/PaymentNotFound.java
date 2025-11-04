package com.securevault.exception.custom;

public class PaymentNotFound extends RuntimeException {

    public PaymentNotFound() {
        super();
    }

    public PaymentNotFound(final String message) {
        super(message);
    }

}
