package com.jsonyao.ttc.listener;

import com.jsonyao.ttc.db143.dao.OrderMapper;
import com.jsonyao.ttc.db143.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 分布式事务: MQ消息方案: RocketMQ消费者消费监听器
 */
@Component(value = "messageListener")
@Slf4j
public class ChangeOrderStatusListener implements MessageListenerConcurrently {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 监听队列, 消费消息, 更新订单状态
     * @param list
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if(CollectionUtils.isEmpty(list)) return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;// 消费成功, 不在消费

        // 默认配置时, 每次只拉取一条消息, 这里为了好回滚就只设置了一条消息一批
        for (MessageExt messageExt : list) {
            String orderId = messageExt.getKeys();
            String msgBody = new String(messageExt.getBody());
            System.out.println("orderId: " + orderId + ", msgBody: " + msgBody);

            // 消费MQ消息: 订单回调接口, 更新订单状态
            try {
                handleOrder(Integer.parseInt(orderId));
            } catch (Exception e) {
                log.error("消费失败, 失败原因: "+ e.getMessage() +", 需要重新消费...");
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;// 再次消费
            }
        }

        log.info("消息消费成功!");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;// 消费成功, 不在消费
    }

    /**
     * 消费MQ消息: 订单回调接口, 更新订单状态
     * @param orderId
     * @return 0:成功 1:订单不存在
     */
    private int handleOrder(int orderId) throws Exception {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if (order==null) throw new RuntimeException("订单不存在!");
        order.setOrderStatus(1);//已支付
        order.setUpdateTime(new Date());
        order.setUpdateUser(0);//系统更新
        orderMapper.updateByPrimaryKey(order);

        // 测试订单操作接口消费支付消息异常: 当前时间戳为奇数时, 则抛出异常, 需要重新消费
        if(System.currentTimeMillis() % 2 == 1){
            int i = 1 / 0;
        }

        return 0;
    }
}
