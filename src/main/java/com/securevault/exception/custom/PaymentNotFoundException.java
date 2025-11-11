package com.securevault.exception.custom;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException() {
        super();
    }

    public PaymentNotFoundException(final String message) {
        super(message);
    }

}
