package com.lwl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayInfoStatusEnum {

    PAID(1),
    ;
    private Integer code;
}
