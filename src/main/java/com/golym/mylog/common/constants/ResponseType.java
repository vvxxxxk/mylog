package com.golym.mylog.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseType {

    SUCCESS("success", true),
    FAIL("fail", false),
    ERROR("error", false);

    private final String message;
    private final boolean status;
}
