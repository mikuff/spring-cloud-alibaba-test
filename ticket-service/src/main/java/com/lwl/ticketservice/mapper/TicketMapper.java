package com.lwl.ticketservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwl.ticketservice.entity.TicketEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TicketMapper extends BaseMapper<TicketEntity> {
    @Update("update ticket set lock_user = #{consumerId} where lock_user is null and  ticket_num = #{ticketNum}")
    Integer lockTicket(@Param("consumerId") Long consumerId, @Param("ticketNum") Long ticketNum);

    @Update("update ticket set owner = #{consumerId},lock_user = NULL where lock_user=#{consumerId} and ticket_num = #{ticketNum}")
    Integer moveTicket(@Param("consumerId") Long consumerId, @Param("ticketNum") Long ticketNum);

    @Update("update ticket set lock_user = null where lock_user is not null and ticket_num = #{ticketNum}")
    Integer unlockTicket(@Param("consumerId") Long consumerId, @Param("ticketNum") Long ticketNum);

}
