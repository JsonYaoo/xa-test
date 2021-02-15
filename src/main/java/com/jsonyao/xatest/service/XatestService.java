package com.jsonyao.xatest.service;

import com.jsonyao.xatest.db142.dao.XA142Mapper;
import com.jsonyao.xatest.db142.model.XA142;
import com.jsonyao.xatest.db143.dao.XA143Mapper;
import com.jsonyao.xatest.db143.model.XA143;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 分布式事务: XA协议-Atomikos测试服务
 */
@Service
public class XatestService {

    @Resource
    private XA142Mapper xa142Mapper;

    @Resource
    private XA143Mapper xa143Mapper;

    /**
     * Atomikos插入服务
     */
    @Transactional(transactionManager = "xaTransactionManager")
    public void testAtomikosInsert(){
        XA142 xa142 = new XA142();
        xa142.setId(1);
        xa142.setName("xa_142");
        xa142Mapper.insert(xa142);

        XA143 xa143 = new XA143();
        xa143.setId(1);
        xa143.setName("xa_143");
        xa143Mapper.insert(xa143);
    }

    /**
     * Atomikos插入但程序异常服务
     */
    @Transactional(transactionManager = "xaTransactionManager")
    public void testAtomikosInsertButException(){
        XA142 xa142 = new XA142();
        xa142.setId(2);
        xa142.setName("xa_142");
        xa142Mapper.insert(xa142);

        XA143 xa143 = new XA143();
        xa143.setId(2);
        xa143.setName("xa_143");
        xa143Mapper.insert(xa143);

        throw new RuntimeException("测试插入异常");
    }

    /**
     * Atomikos数据库插入异常服务
     */
    @Transactional(transactionManager = "xaTransactionManager")
    public void testAtomikosException(){
        XA142 xa142 = new XA142();
        xa142.setId(3);
        xa142.setName("xa_142");// 数据库name字段设置为varchar(2)
        xa142Mapper.insert(xa142);

        XA143 xa143 = new XA143();
        xa143.setId(3);
        xa143.setName("xa_143");// 数据库name字段设置为varchar(2)
        xa143Mapper.insert(xa143);
    }
}
