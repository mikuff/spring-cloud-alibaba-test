package com.lwl.result;

import com.lwl.enums.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private String code;
    private String msg;
    private T data;


    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setMsg(ResponseEnum.OK.getMsg());
        result.setCode(ResponseEnum.OK.getCode());
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResponseEnum.OK.getCode());
        result.setMsg(ResponseEnum.OK.getMsg());
        return result;
    }

    public static <T> Result<T> fail(ResponseEnum responseEnum) {
        Result<T> result = new Result<>();
        result.setMsg(responseEnum.getMsg());
        result.setCode(responseEnum.getCode());
        return result;
    }

    public static <T> Result<T> fail(ResponseEnum responseEnum, T data) {
        Result<T> result = new Result<>();
        result.setMsg(responseEnum.getMsg());
        result.setCode(responseEnum.getCode());
        result.setData(data);
        return result;
    }

}
