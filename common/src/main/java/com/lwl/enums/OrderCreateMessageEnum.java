package com.lwl.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderCreateMessageEnum {


    TICKET_LOCKED(0, null),

    ORDER_NEW(10, null),

    ORDER_PAID(20, null),

    TICKET_MOVED(30, null),

    TICKET_LOCKED_FAIL(1000, "索票失败"),
    ;
    private Integer code;
    private String message;


    /**
     * 通过code获取具体的错误信息
     * @param code
     * @return
     */
    private String getMessageByCode(Integer code) {
        for (OrderCreateMessageEnum item : OrderCreateMessageEnum.values()) {
            if (Objects.equals(item.getCode(), code)) {
                return item.getMessage();
            }
        }
        return "未知原因: code(" + String.valueOf(code) + ")";
    }
}
