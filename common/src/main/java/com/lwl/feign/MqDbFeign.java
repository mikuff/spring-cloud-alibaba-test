package com.lwl.feign;

import com.lwl.dto.ChainInfoDTO;
import com.lwl.dto.MqDbDTO;

public interface MqDbFeign {

    Integer insertDb(MqDbDTO dto);
    Integer sendMessage(String message);

    Integer sendMessageAndInsertDb(MqDbDTO dto);


}
