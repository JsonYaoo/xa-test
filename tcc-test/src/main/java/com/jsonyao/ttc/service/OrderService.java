package com.jsonyao.ttc.service;

import com.jsonyao.ttc.db143.dao.OrderMapper;
import com.jsonyao.ttc.db143.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 分布式事务: 本地消息表方案 & MQ消息方案测试服务\
 */
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 本地消息表服务: 订单回调接口, 更新订单状态
     * @param orderId
     * @return 0:成功 1:订单不存在
     */
    @Transactional
    public int handleOrder(int orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if (order==null) return 1;
        order.setOrderStatus(1);//已支付
        order.setUpdateTime(new Date());
        order.setUpdateUser(0);//系统更新
        orderMapper.updateByPrimaryKey(order);

        // 测试订单操作接口更新异常
        int i = 1 / 0;

        return 0;
    }

}
