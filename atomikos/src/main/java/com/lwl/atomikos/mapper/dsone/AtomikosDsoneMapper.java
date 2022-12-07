package com.lwl.atomikos.mapper.dsone;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.atomikos.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AtomikosDsoneMapper extends BaseMapper<UserEntity> {
}
