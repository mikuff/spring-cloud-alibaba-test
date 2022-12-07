package com.lwl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    CREATE(0),
    SUCCESS(1),
    ERROR(-1),
    ;
    private Integer code;
}
