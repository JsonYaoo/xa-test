package com.jsonyao.xatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式事务: XA协议数据源-Atomikos测试应用
 */
@SpringBootApplication
public class XatestApplication {

    public static void main(String[] args) {
        SpringApplication.run(XatestApplication.class, args);
    }
}
