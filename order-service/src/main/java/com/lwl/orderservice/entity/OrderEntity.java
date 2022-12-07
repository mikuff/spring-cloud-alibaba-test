package com.lwl.orderservice.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("orders")
@Data
@NoArgsConstructor
public class OrderEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    /**
     * uuid 用于做幂等去重
     */
    private String uuid;

    /**
     * 消费者ID，表示订单时谁发起
     */
    private Long consumerId;

    /**
     * 票号,买的那张表
     */
    private Long ticketNum;

    /**
     * 金额
     */
    private Long amount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String reason;

    private Date createDate;
}
