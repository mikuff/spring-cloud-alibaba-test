package com.lwl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    OK("00000", "ok"),
    FAIL("F00000", "系统异常"),


    // ticket 服务 10000
    NOT_FIND_TICKET("T00001", "无法查询到票据"),

    // order 服务 20000
    CREATE_ORDER_FAIL("O00001", "创建订单失败"),


    // user 服务 30000

    ;

    private final String code;

    private final String msg;

}
