package com.lwl.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderDTO {
    private String uuid;
    private Long consumerId;
    private Long ticketNum;
    private Integer status;
}
