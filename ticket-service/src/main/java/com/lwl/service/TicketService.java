package com.lwl.service;

import com.lwl.entity.TicketEntity;
import com.lwl.dto.OrderDTO;

public interface TicketService {
    TicketEntity lock(OrderDTO orderDTO);
}
