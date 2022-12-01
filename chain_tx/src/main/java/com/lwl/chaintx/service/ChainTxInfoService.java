package com.lwl.chaintx.service;

import com.lwl.dto.ChainInfoDTO;

public interface ChainTxInfoService {

    ChainInfoDTO getInfoByDbOne(Integer id);

    ChainInfoDTO getInfoByDbTwo(Integer id);

    Integer insertDbOne(ChainInfoDTO dto);

    Integer insertDbTwo(ChainInfoDTO dto);


    Integer insertDbOneError(ChainInfoDTO dto);


    Integer insertDbTwoError(ChainInfoDTO dto);

    Integer insertAllDbNormal(ChainInfoDTO dto);

    Integer insertAllDbError(ChainInfoDTO dto);

}
