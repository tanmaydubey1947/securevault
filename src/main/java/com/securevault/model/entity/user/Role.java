package com.securevault.model.entity.user;

public enum Role {

    ROLE_USER,
    ROLE_ADMIN;

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (role.equalsIgnoreCase(r.name())) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + role);
    }
}
