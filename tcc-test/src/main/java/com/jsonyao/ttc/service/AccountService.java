package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.AccountAMapper;
import com.jsonyao.ttc.db142.model.AccountA;
import com.jsonyao.ttc.db143.dao.AccountBMapper;
import com.jsonyao.ttc.db143.model.AccountB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 分布式事务TCC事务回滚机制测试服务
 */
@Service
public class AccountService {

    @Resource
    private AccountAMapper accountAMapper;

    @Resource
    private AccountBMapper accountBMapper;

    /**
     * 测试多数据源异常时: 默认事务管理器是否能够回滚所有数据源
     */
    @Transactional
    public void defalutTransferAccount(){
        // A账户减200
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        int i = 1 / 0;

        // B账户加200
        AccountB accountB = accountBMapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
        accountBMapper.updateByPrimaryKey(accountB);
    }
}
