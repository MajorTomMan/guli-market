package com.atguigu.gulimall.ware.config;

import java.util.HashMap;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WareRabbitMQConfig {
    /* 
     * 
     */
    @Bean
    public Exchange stockEventExchange() {

        return new TopicExchange("stock-event-exchange", true, false);
    }



    /**
     * 普通队列，用于解锁库存
     * 
     * @return
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        Queue queue = new Queue("stock.release.stock.queue", true, false, false);
        return queue;
    }

    /**
     * 延迟队列
     * 
     * @return
     */
    @Bean
    public Queue stockDelayQueue() {
        /*
         * x-dead-letter-exchange:stock-event-exchange
         * x-dead-letter-routing-key:stock.release
         * // 消息过期时间 2分钟
         * x-message-ttl:120000
         */
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "stock.release");
        // 消息过期时间 2分钟
        arguments.put("x-message-ttl", 120000);
        Queue queue = new Queue("stock.delay.queue", true, false, false, arguments);
        return queue;
    }

    /**
     * 交换机和延迟队列绑定
     * 
     * @return
     */
    @Bean
    public Binding stockLockedBinding() {
        return new Binding("stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                null);
    }

    /**
     * 交换机和普通队列绑定
     * 
     * @return
     */
    @Bean
    public Binding stockReleaseBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                null);
    }
}
