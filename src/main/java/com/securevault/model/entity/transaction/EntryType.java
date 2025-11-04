package com.securevault.model.entity.transaction;

public enum EntryType {

    DEBIT,
    CREDIT;

    public static EntryType fromString(String status) {
        if (status != null) {
            for (EntryType r : EntryType.values()) {
                if (status.equalsIgnoreCase(r.name())) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No enum constant " + EntryType.class.getCanonicalName() + "." + status);
    }
}
