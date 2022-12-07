package com.lwl.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.userservice.entity.ConsumerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ConsumerMapper extends BaseMapper<ConsumerEntity> {

    @Update("update consumer set deposit = deposit - #{amount} where id = #{consumerId}")
    int charge(@Param("consumerId") Long consumerId, @Param("amount") Long amount);
}
