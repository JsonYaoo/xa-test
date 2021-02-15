package com.jsonyao.ttc.controller;

import com.jsonyao.ttc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式事务: 本地消息表方案 & MQ消息方案测试服务前端控制器
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 本地消息表服务: 订单回调接口, 更新订单状态
     * @param orderId
     * @return 0:成功 1:订单不存在
     */
    @RequestMapping("handleOrder")
    public String handleOrder(int orderId){
        try{
            int result = orderService.handleOrder(orderId);
            if (result == 0) return "success";
            return "fail";
        }catch (Exception e){
            return "fail";
        }
    }
}
