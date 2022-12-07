package com.lwl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderContextDTO {
    private Long orderId;
    private Long consumerId;
    private Long amount;
    private Integer status;
    private String uuid;
    private String title;
    private Long ticketNum;
}
