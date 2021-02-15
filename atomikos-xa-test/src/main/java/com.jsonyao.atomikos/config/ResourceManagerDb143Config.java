package com.jsonyao.atomikos.config;

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
 * RM: XA_143数据源 & MyBatis配置类
 */
@Configuration
// 扫描Mapper.java
@MapperScan(value = "com.jsonyao.atomikos.db143.dao", sqlSessionFactoryRef = "sqlSessionFactoryBean143")
public class ResourceManagerDb143Config {

    /**
     * XA_143数据源配置
     * @return
     */
    @Bean("db143")
    public DataSource db143(){
        // Mysql XA数据源
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl("jdbc:mysql://192.168.1.143:3306/xa_143");
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
    @Bean("sqlSessionFactoryBean143")
    public SqlSessionFactoryBean sqlSessionFactoryBean143(@Qualifier("db143") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("mybatis/db143/*.xml"));
        return sqlSessionFactoryBean;
    }
}
