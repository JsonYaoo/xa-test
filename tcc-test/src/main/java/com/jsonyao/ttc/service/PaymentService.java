package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db142.dao.AccountAMapper;
import com.jsonyao.ttc.db142.dao.PaymentMsgMapper;
import com.jsonyao.ttc.db142.model.AccountA;
import com.jsonyao.ttc.db142.model.PaymentMsg;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DefaultMQProducer producer;

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

    /**
     * MQ消息投递: 执行支付、投递消息
     * @param userId
     * @param orderId
     * @param amount
     * @return 0:成功；1:用户不存在;2:余额不足
     */
    @Transactional(transactionManager = "transactionManager142", rollbackFor = Exception.class)
    public int pamentMQ(int userId, int orderId, BigDecimal amount) throws Exception {
        // 支付操作
        AccountA accountA = accountAMapper.selectByPrimaryKey(userId);
        if (accountA == null) return 1;
        if (accountA.getBalance().compareTo(amount) < 0) return 2;
        accountA.setBalance(accountA.getBalance().subtract(amount));
        accountAMapper.updateByPrimaryKey(accountA);

        // 构造消息
        Message message = new Message();
        message.setTopic("payment");
        message.setKeys(orderId + "");
        message.setBody("订单已支付".getBytes());

        // 发送消息到MQ
        try {
            SendResult sendResult = producer.send(message);
            if(sendResult.getSendStatus() == SendStatus.SEND_OK){
                return 0;
            }else {
                throw new Exception("消息发送失败！");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
