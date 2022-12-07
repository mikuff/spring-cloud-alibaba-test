package com.lwl.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.orderservice.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}
