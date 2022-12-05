package com.lwl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MqDbDTO {

    private String name;
    private Integer age;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy/MM/dd")
    private Date createDate;

    private String message;
}
