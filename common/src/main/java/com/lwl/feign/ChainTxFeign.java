package com.lwl.feign;

import com.lwl.dto.ChainInfoDTO;

public interface ChainTxFeign {

    ChainInfoDTO getInfoByDbOne(Integer id);

    ChainInfoDTO getInfoByDbTwo(Integer id);

    Integer insertDbOne(ChainInfoDTO dto);

    Integer insertDbTwo(ChainInfoDTO id);

    Integer insertDbOneError(ChainInfoDTO dto);

    Integer insertDbTwoError(ChainInfoDTO id);

    Integer insertAllDbNormal(ChainInfoDTO dto);

    Integer insertAllDbError(ChainInfoDTO dto);
}
