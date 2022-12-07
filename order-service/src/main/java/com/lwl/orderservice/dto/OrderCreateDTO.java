package com.lwl.orderservice.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private Long consumerId;
    private Long ticketNum;
    private String title;
    private Long amount;
    private String uuid;
}
