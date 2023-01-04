package com.lwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@TableName("ticket")
public class TicketEntity implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Long owner;
    private Long ticketNum;
    private Long lockUser;
}
