package com.lwl.atomikos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("test_atomikos")
public class UserEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String address;
    private Date createDate;

}
