package com.jsonyao.ttc.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式事务: MQ消息方案: RocketMQ配置类
 */
@Configuration
public class RocketmqConfig {

    /**
     * 配置生产者: 生命周期等同于Spring的生命周期
     * @return
     */
    @Bean(value = "producer", initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer producer(){
        DefaultMQProducer producer = new DefaultMQProducer("paymentGroup");
        producer.setNamesrvAddr("localhost:9876");
        return producer;
    }

    /**
     * 配置生产者: 生命周期等同于Spring的生命周期
     * @return
     */
    @Bean(value = "consumer", initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer consumer(@Qualifier("messageListener") MessageListenerConcurrently messageListener){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("paymentConsumerGroup");
        consumer.setNamesrvAddr("localhost:9876");
        try {
            consumer.subscribe("payment", "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.registerMessageListener(messageListener);
        return consumer;
    }
}
