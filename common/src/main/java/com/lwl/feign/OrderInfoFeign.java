package com.lwl.feign;

import com.lwl.dto.OrderInfoDTO;

public interface OrderInfoFeign {
    OrderInfoDTO getInfoById(Integer id);
}
