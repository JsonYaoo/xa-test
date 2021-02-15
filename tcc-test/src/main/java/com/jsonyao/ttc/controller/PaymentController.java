package com.jsonyao.ttc.controller;

import com.jsonyao.ttc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 分布式事务: 本地消息表方案 & MQ消息方案测试服务前端控制器
 */
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 本地消息表服务: 执行支付、保存本地消息
     * @param userId
     * @param orderId
     * @param amount
     * @return
     * @throws Exception
     */
    @RequestMapping("payment")
    public String payment(int userId, int orderId, BigDecimal amount) throws Exception {
        int result = paymentService.pament(userId, orderId, amount);
        return "支付结果："+result;
    }

    /**
     * MQ消息投递: 执行支付、投递消息
     * @param userId
     * @param orderId
     * @param amount
     * @return
     * @throws Exception
     */
    @RequestMapping("paymentMQ")
    public String paymentMQ(int userId, int orderId, BigDecimal amount) throws Exception {
        int result = paymentService.pamentMQ(userId, orderId, amount);
        return "支付结果："+result;
    }
}
