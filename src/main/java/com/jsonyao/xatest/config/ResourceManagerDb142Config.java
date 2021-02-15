package com.jsonyao.xatest.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * RM: XA_142数据源 & MyBatis配置类
 */
@Configuration
// 扫描Mapper.java
@MapperScan(value = "com.jsonyao.xatest.db142.dao", sqlSessionFactoryRef = "sqlSessionFactoryBean142")
public class ResourceManagerDb142Config {

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

        // 包装到Atomikos数据源中并作为数据源返回
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);
        return atomikosDataSourceBean;
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
