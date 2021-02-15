package com.jsonyao.mycat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式事务: XA协议-MyCat测试应用
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.jsonyao.mycat.dao"})
public class MycatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycatApplication.class, args);
    }
}
