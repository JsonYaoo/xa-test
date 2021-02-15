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
     * => 结果: 只配置了A事务管理器, 所有数据源并没有全部发生回滚, A账户回滚了, 但B账户多了200,
     *
     */
    @Test
    public void aTransferAccount(){
        accountService.aTransferAccount();
    }

    /**
     * 测试多数据源异常时: 配了A事务管理器, 且对B数据源做事务补偿
     * => 结果:
     *      1) 搞不好还会引发事务没关闭现象 => 查询正在运行的事务: SELECT * FROM information_schema.INNODB_TRX;
     *      2) 补偿逻辑复杂, 调试成本高, 容易出错, 所以不推荐项目使用事务补偿机制
     */
    @Test
    public void tccTransferAccount(){
        accountService.tccTransferAccount();
    }
}
