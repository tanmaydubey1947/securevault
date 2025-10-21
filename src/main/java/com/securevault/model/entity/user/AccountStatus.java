package com.securevault.model.entity.user;

public enum AccountStatus {

    ACTIVE,
    SUSPENDED,
    CLOSED,
    PENDING_VERIFICATION;

    public static AccountStatus fromString(String status) {
        if (status != null) {
            for (AccountStatus r : AccountStatus.values()) {
                if (status.equalsIgnoreCase(r.name())) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + AccountStatus.class.getCanonicalName() + "." + status);
    }
}
