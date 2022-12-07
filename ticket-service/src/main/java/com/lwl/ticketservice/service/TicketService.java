package com.lwl.ticketservice.service;

import com.lwl.exception.BussniessException;
import com.lwl.ticketservice.dto.OrderDTO;
import com.lwl.ticketservice.entity.TicketEntity;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface TicketService {
    TicketEntity lock(OrderDTO orderDTO);
}
