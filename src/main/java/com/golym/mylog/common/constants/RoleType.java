package com.golym.mylog.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    ROLE_ADMIN("R0000"),
    ROLE_USER("R0100");

    private final String ROLE_ID;

    public static RoleType of(String id) {
        for (RoleType roleType : values()) {
            if (roleType.ROLE_ID.equals(id))
                return roleType;
        }
        throw new IllegalArgumentException("Unknown role id: " + id);
    }
}
