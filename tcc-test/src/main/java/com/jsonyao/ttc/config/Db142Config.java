package com.jsonyao.ttc.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * XA_142数据源 & MyBatis配置类
 */
@Configuration
// 扫描Mapper.java
@MapperScan(value = "com.jsonyao.ttc.db142.dao", sqlSessionFactoryRef = "sqlSessionFactoryBean142")
public class Db142Config {

    /**
     * XA_142数据源配置
     * @return
     */
    @Bean("db142")
    public DataSource db142(){
        // Mysql XA数据源
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl("jdbc:mysql://192.168.1.142:3306/xa_142");
        xaDataSource.setUser("imooc");
        xaDataSource.setPassword("Imooc@123456");
        return xaDataSource;
    }

    /**
     * SqlSessionFactoryBean配置: 扫描Mapping.xml
     * @param dataSource
     * @return
     * @throws IOException
     */
    @Bean("sqlSessionFactoryBean142")
    public SqlSessionFactoryBean sqlSessionFactoryBean142(@Qualifier("db142") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("mybatis/db142/*.xml"));
        return sqlSessionFactoryBean;
    }
}
