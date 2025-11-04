package com.securevault.model.entity.transaction;

public enum TransactionStatus {

    PENDING,
    SUCCESS,
    FAILED,
    ACKNOWLEDGED,
    INITIATED;

    public static TransactionStatus fromString(String status) {
        if (status != null) {
            for (TransactionStatus r : TransactionStatus.values()) {
                if (status.equalsIgnoreCase(r.name())) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + TransactionStatus.class.getCanonicalName() + "." + status);
    }
}
