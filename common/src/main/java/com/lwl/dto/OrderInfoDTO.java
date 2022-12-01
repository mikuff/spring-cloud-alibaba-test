package com.lwl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class OrderInfoDTO implements Serializable {
    private Integer id;
    private String text;
}
