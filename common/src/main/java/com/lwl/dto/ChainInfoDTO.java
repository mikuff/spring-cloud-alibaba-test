package com.lwl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChainInfoDTO implements Serializable {
    private String name;
    private Integer age;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy/MM/dd")
    private Date createDate;
}
