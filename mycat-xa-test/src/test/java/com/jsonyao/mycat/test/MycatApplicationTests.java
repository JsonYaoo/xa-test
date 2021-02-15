package com.jsonyao.mycat.test;

import com.jsonyao.mycat.MycatApplication;
import com.jsonyao.mycat.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 分布式事务: XA协议-MyCat测试应用测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MycatApplication.class})
public class MycatApplicationTests {

    @Autowired
    private UserService userService;

    /**
     * MyCat数据库插入异常服务
     * => 测试结果: 成功回滚
     */
    @Test
    public void testUser(){
        userService.testUser();
    }
}
