package com.jsonyao.atomikos.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

/**
 * TM: XA协议Atomikos事务管理器
 */
@Configuration
public class XaTransactionManagerConfig {

    /**
     * XA协议Atomikos事务管理器
     * @return
     */
    @Bean("xaTransactionManager")
    public JtaTransactionManager xaTransactionManager(){
        UserTransaction userTransaction = new UserTransactionImp();
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
