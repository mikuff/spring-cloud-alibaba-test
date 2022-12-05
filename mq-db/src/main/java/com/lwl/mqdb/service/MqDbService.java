package com.lwl.mqdb.service;

import com.lwl.dto.MqDbDTO;

public interface MqDbService {

    Integer insertDb(MqDbDTO dto);

    Integer sendMessage(String message);

    Integer sendMessageAndInsertDb(MqDbDTO dto);

}
