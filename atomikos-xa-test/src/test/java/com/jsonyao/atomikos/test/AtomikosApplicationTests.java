package com.jsonyao.atomikos.test;

import com.jsonyao.atomikos.AtomikosApplication;
import com.jsonyao.atomikos.service.XatestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 分布式事务: XA协议数据源-Atomikos测试应用测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AtomikosApplication.class})
public class AtomikosApplicationTests {

    @Autowired
    private XatestService xatestService;

    /**
     * 测试Atomikos插入服务
     * => 测试结果: 成功插入
     */
    @Test
    public void testAtomikosInsert(){
        xatestService.testAtomikosInsert();
    }

    /**
     * 测试Atomikos插入但程序异常服务
     * => 测试结果: 成功回滚
     */
    @Test
    public void testAtomikosInsertButException(){
        xatestService.testAtomikosInsertButException();
    }

    /**
     * 测试Atomikos数据库插入异常服务
     * => 测试结果: 成功回滚
     */
    @Test
    public void testAtomikosException(){
        xatestService.testAtomikosException();
    }
}
