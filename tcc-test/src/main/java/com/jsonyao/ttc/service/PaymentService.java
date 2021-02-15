package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.AccountAMapper;
import com.jsonyao.ttc.db142.dao.PaymentMsgMapper;
import com.jsonyao.ttc.db142.model.AccountA;
import com.jsonyao.ttc.db142.model.PaymentMsg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分布式事务: 本地消息表方案 & MQ消息方案测试服务
 */
@Service
public class PaymentService {

    @Resource
    private AccountAMapper accountAMapper;

    @Resource
    private PaymentMsgMapper paymentMsgMapper;

    /**
     * 本地消息表服务: 执行支付、保存本地消息
     * @param userId
     * @param orderId
     * @param amount
     * @return 0:成功；1:用户不存在;2:余额不足
     */
    @Transactional(transactionManager = "transactionManager142")
    public int pament(int userId, int orderId, BigDecimal amount){
        //支付操作
        AccountA accountA = accountAMapper.selectByPrimaryKey(userId);
        if (accountA == null) return 1;
        if (accountA.getBalance().compareTo(amount) < 0) return 2;
        accountA.setBalance(accountA.getBalance().subtract(amount));
        accountAMapper.updateByPrimaryKey(accountA);

        PaymentMsg paymentMsg = new PaymentMsg();
        paymentMsg.setOrderId(orderId);
        paymentMsg.setStatus(0);//未发送
        paymentMsg.setFalureCnt(0);//失败次数
        paymentMsg.setCreateTime(new Date());
        paymentMsg.setCreateUser(userId);
        paymentMsg.setUpdateTime(new Date());
        paymentMsg.setUpdateUser(userId);

        paymentMsgMapper.insertSelective(paymentMsg);
        return 0;
    }
}
