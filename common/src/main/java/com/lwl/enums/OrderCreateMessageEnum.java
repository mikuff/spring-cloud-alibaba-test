package com.lwl.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderCreateMessageEnum {

    TICKET_LOCKED(0),

    ORDER_NEW(10),

    ORDER_PAID(20),

    TICKET_MOVED(30)
    ;
    private Integer code;
}
