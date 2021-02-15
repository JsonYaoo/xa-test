package com.jsonyao.ttc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式事务TCC事务回滚机制: Try-Confirm-Cancel测试应用
 */
@SpringBootApplication
public class TccApplication {

    public static void main(String[] args) {
        SpringApplication.run(TccApplication.class, args);
    }
}
