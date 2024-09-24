package com.atguigu.gulimall.order.config;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* 
 *  路径 生产者-> 交换机->延时队列->队列->消费者
 *                                order-event-exchange->order.delay.queue->order.release.order.queue
 * 
 * 
 * 
 */
@Configuration
public class OrderRabbitMQConfig {
    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * 延迟队列
     * 
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        // 死信交换机
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        // 死信路由键
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", Duration.ofMinutes(30).toMillis()); // 消息过期时间 1分钟
        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    /**
     * 普通队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue("order.release.order.queue", true, false, false);

        return queue;
    }

    /**
     * 创建订单的binding
     * 
     * @return
     */
    @Bean
    public Binding orderCreateBinding() {
        /**
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         */
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE, "order-event-exchange",
                "order.create.order", null);
    }

    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    /*
     * 该绑定是为了解决假设库存解锁时查询订单服务因为订单服务器出于各种原因的情况下
     * 没能成功处理取消订单,而导致此时订单还处于新建状态,
     * 进而使得消息被消费而无法成功解锁库存的情况
     */
    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }

    /**
     * 商品秒杀队列
     * 
     * @return
     */
    @Bean
    public Queue orderSecKillOrrderQueue() {
        Queue queue = new Queue("order.seckill.order.queue", true, false, false);
        return queue;
    }

    @Bean
    public Binding orderSecKillOrrderQueueBinding() {
        Binding binding = new Binding(
                "order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                null);

        return binding;
    }

}
