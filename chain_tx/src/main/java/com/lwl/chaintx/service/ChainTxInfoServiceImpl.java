package com.lwl.chaintx.service;

import com.lwl.chaintx.entity.ChainTxInfoEntity;
import com.lwl.chaintx.mapper.dsone.DsOneChainTxInfoMapper;
import com.lwl.chaintx.mapper.dstwo.DsTwoChainTxInfoMapper;
import com.lwl.dto.ChainInfoDTO;
import io.netty.util.internal.ObjectUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChainTxInfoServiceImpl implements ChainTxInfoService {

    @Autowired
    private DsOneChainTxInfoMapper dsOneChainTxInfoMapper;

    @Autowired
    private DsTwoChainTxInfoMapper dsTwoChainTxInfoMapper;

    @Override
    public ChainInfoDTO getInfoByDbOne(Integer id) {
        ChainTxInfoEntity chainTxInfoEntity = dsOneChainTxInfoMapper.selectById(id);
        ChainInfoDTO ret = new ChainInfoDTO();
        BeanUtils.copyProperties(chainTxInfoEntity, ret);
        return ret;
    }

    @Override
    public ChainInfoDTO getInfoByDbTwo(Integer id) {
        ChainTxInfoEntity chainTxInfoEntity = dsTwoChainTxInfoMapper.selectById(id);
        ChainInfoDTO ret = new ChainInfoDTO();
        BeanUtils.copyProperties(chainTxInfoEntity, ret);
        return ret;
    }

    @Override
    public Integer insertDbOne(ChainInfoDTO dto) {

        ChainTxInfoEntity insert = new ChainTxInfoEntity();
        BeanUtils.copyProperties(dto, insert);
        return dsOneChainTxInfoMapper.insert(insert);
    }

    @Override
    public Integer insertDbTwo(ChainInfoDTO dto) {
        ChainTxInfoEntity insert = new ChainTxInfoEntity();
        BeanUtils.copyProperties(dto, insert);
        return dsTwoChainTxInfoMapper.insert(insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "dsOneTransactionManager")
    public Integer insertDbOneError(ChainInfoDTO dto) {
        ChainTxInfoEntity insert = new ChainTxInfoEntity();
        BeanUtils.copyProperties(dto, insert);
        dsOneChainTxInfoMapper.insert(insert);
        throw new RuntimeException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "dsTwoTransactionManager")
    public Integer insertDbTwoError(ChainInfoDTO dto) {
        ChainTxInfoEntity insert1 = new ChainTxInfoEntity();
        ChainTxInfoEntity insert2 = new ChainTxInfoEntity();

        BeanUtils.copyProperties(dto, insert1);
        BeanUtils.copyProperties(dto, insert2);

        insert1.setName(insert1.getName()+"_dsOne");
        insert2.setName(insert2.getName()+"_dsTwo");

        System.out.println("dsOne ======================================================================================");
        dsOneChainTxInfoMapper.insert(insert1);
        System.out.println("dsTwo ======================================================================================");
        dsTwoChainTxInfoMapper.insert(insert2);
        System.out.println("throw exception ======================================================================================");
        throw new RuntimeException();
    }

    @Override
    @Transactional
    public Integer insertAllDbNormal(ChainInfoDTO dto) {
        ChainTxInfoEntity insert = new ChainTxInfoEntity();
        BeanUtils.copyProperties(dto, insert);
        Integer ret1 = dsOneChainTxInfoMapper.insert(insert);
        Integer ret2 = dsTwoChainTxInfoMapper.insert(insert);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "chainedTransactionManager")
    public Integer insertAllDbError(ChainInfoDTO dto) {
        ChainTxInfoEntity insert1 = new ChainTxInfoEntity();
        ChainTxInfoEntity insert2 = new ChainTxInfoEntity();

        BeanUtils.copyProperties(dto, insert1);
        BeanUtils.copyProperties(dto, insert2);

        insert1.setName(insert1.getName()+"_dsOne");
        insert2.setName(insert2.getName()+"_dsTwo");

        System.out.println("dsOne ======================================================================================");
        dsOneChainTxInfoMapper.insert(insert1);

        System.out.println("dsTwo ======================================================================================");
        dsTwoChainTxInfoMapper.insert(insert2);

        System.out.println("throw exception ======================================================================================");
        throw new RuntimeException();
    }
}
