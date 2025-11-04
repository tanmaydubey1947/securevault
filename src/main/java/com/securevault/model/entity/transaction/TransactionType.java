package com.securevault.model.entity.transaction;

public enum TransactionType {

    TOPUP,
    WITHDRAW,
    TRANSFER,
    RECEIVE,
    REFUND,
    ADJUSTMENT;

    public static TransactionType fromString(String status) {
        if (status != null) {
            for (TransactionType r : TransactionType.values()) {
                if (status.equalsIgnoreCase(r.name())) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + TransactionType.class.getCanonicalName() + "." + status);
    }
}
