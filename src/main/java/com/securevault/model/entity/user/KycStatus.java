package com.securevault.model.entity.user;

public enum KycStatus {

    PENDING,
    VERIFIED,
    REJECTED;

    public static KycStatus fromString(String status) {
        if (status != null) {
            for (KycStatus r : KycStatus.values()) {
                if (status.equalsIgnoreCase(r.name())) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + KycStatus.class.getCanonicalName() + "." + status);
    }
}
