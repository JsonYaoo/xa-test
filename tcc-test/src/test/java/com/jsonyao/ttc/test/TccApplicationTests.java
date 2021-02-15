package com.jsonyao.ttc.test;

import com.jsonyao.ttc.TccApplication;
import com.jsonyao.ttc.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 分布式事务TCC事务回滚机制: Try-Confirm-Cancel测试应用测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TccApplication.class})
public class TccApplicationTests {

    @Autowired
    private AccountService accountService;

    /**
     * 测试多数据源异常时: 默认事务管理器是否能够回滚所有数据源
     * => 结果: 多数据源时, 默认的事务管理器并没有发生回滚, A账户减了200, B账户还没更新
     */
    @Test
    public void defalutTransferAccount(){
        accountService.defalutTransferAccount();
    }

    /**
     * 测试多数据源异常时: 只配置了A事务管理器是否能够回滚所有数据源
     * => 结果: 只配置了A事务管理器, 所有数据源并没有全部发生回滚, A账户回滚了, 但B账户多了200
     */
    @Test
    public void aTransferAccount(){
        accountService.aTransferAccount();
    }
}
