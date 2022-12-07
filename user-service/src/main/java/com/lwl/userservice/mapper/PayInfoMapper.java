package com.lwl.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.userservice.entity.ConsumerEntity;
import com.lwl.userservice.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayInfoMapper extends BaseMapper<PayInfoEntity> {
}
