package com.lwl.orderservice.service;

import com.lwl.orderservice.dto.OrderCreateDTO;
import com.lwl.orderservice.entity.OrderEntity;

import java.util.List;

public interface OrderService {

    /**
     * 根据消费者ID查询消费者订单
     * @param consumerId 消费者ID
     * @return
     */
    List<OrderEntity> findListByConsumerId(Long consumerId);

    /**
     * 根据UUID查询订单信息
     * @param uuid UUID
     * @return
     */
    Long findOneByUuid(String uuid);

    void createOrder(OrderCreateDTO orderDTO);

}
