package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.AccountAMapper;
import com.jsonyao.ttc.db142.model.AccountA;
import com.jsonyao.ttc.db143.dao.AccountBMapper;
import com.jsonyao.ttc.db143.model.AccountB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 分布式事务TCC事务回滚机制测试服务
 */
@Service
@Slf4j
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

    /**
     * 测试多数据源异常时: 只配置了A事务管理器是否能够回滚所有数据源
     */
    @Transactional(transactionManager = "transactionManager142")
    public void aTransferAccount(){
        // A账户减200
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        // B账户加200
        AccountB accountB = accountBMapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
        accountBMapper.updateByPrimaryKey(accountB);

        int i = 1 / 0;
    }

    /**
     * 测试多数据源异常时: 配了A事务管理器, 且对B数据源做事务补偿
     */
    @Transactional(transactionManager = "transactionManager142", rollbackFor = Exception.class)
    public void tccTransferAccount(){
        // A账户减200
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        // B账户加200
        AccountB accountB = accountBMapper.selectByPrimaryKey(2);

        // 备份B账户信息
        AccountB bakAccountB = new AccountB();
        BeanUtils.copyProperties(accountB, bakAccountB);

        // 更新B账户
        try {
            accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
            accountBMapper.updateByPrimaryKey(accountB);
            int i = 1 / 0;
        }catch (Exception e) {
            // 循环补偿条件
            int maxTriesTimes = 3;// 初始1次 + 重试3次 = 最大补偿4次
            Exception whileException = new Exception(e);

            // 进行事务补偿: B账户回滚
            int times = 0;
            while (times < maxTriesTimes && whileException != null) {
                try {
//                    int j = 1 / 0;
                    accountBMapper.updateByPrimaryKey(bakAccountB);
                    whileException = null;
                }catch (Exception e2) {
                    // 补偿事务进行循环补偿
                    whileException = e2;
                    times++;
                    log.info("事务补偿失败, 正在重试第"+ times + "次...");
                }
            }

            // 超过重试次数则记录日志并通知工作人员进行人工处理
            if(times >= maxTriesTimes){
                log.info("事务补偿了" + (times+1) + "次都已失败, 失败原因: "+ e.getMessage() +", 请人工处理!");
            }else {
                // 事务补偿成功
                log.info("第" + (times+1) + "次进行事务补偿成功, 账户B回滚成功!");
            }

            throw e;
        }
    }
}
