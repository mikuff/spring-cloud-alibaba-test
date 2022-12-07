package com.lwl.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@TableName("pay_info")
@ToString
public class PayInfoEntity {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long orderId;

    private Integer status;

    private Long amount;
}
