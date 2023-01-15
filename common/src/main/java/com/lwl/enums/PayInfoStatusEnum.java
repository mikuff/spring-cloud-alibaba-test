package com.lwl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayInfoStatusEnum {

    PAID(1),
    NOT_PAID(0),
    ;
    private Integer code;
}
