package com.lwl.mqdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName(value = "mq_db_info")
public class MqDbInfoEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
    private Date createDate;
}
