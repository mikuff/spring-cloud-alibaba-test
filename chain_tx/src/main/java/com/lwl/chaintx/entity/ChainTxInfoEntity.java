package com.lwl.chaintx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName(value = "chain_tx_info")
public class ChainTxInfoEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
    private Date createDate;
}
