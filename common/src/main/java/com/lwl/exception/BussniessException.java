package com.lwl.exception;

import com.lwl.enums.ResponseEnum;

public class BussniessException extends RuntimeException {

    private ResponseEnum responseEnum;

    public BussniessException() {
    }

    public BussniessException(String message) {
        super(message);
    }

    public BussniessException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
    }
}
