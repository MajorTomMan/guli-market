package com.atguigu.gulimall.common.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

/**
 * 定制RabbitTemplate
 * 1、服务收到消息就会回调
 * 1、spring.rabbitmq.publisher-confirms: true
 * 2、设置确认回调
 * 2、消息正确抵达队列就会进行回调
 * 1、spring.rabbitmq.publisher-returns: true
 * spring.rabbitmq.template.mandatory: true
 * 2、设置确认回调ReturnCallback
 *
 * 3、消费端确认(保证每个消息都被正确消费，此时才可以broker删除这个消息)
 *
 */

// 回调接口
@Log4j2
@Configuration
public class RabbitMessageCallBackConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct // 在rabbitTemeplate注入完成后执行这个自动初始化方法
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 只要消息没有投递给指定的队列，就触发这个失败回调
     * message：投递失败的消息详细信息
     * replyCode：回复的状态码
     * replyText：回复的文本内容
     * exchange：当时这个消息发给哪个交换机
     * routingKey：当时这个消息用哪个路邮键
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info(returned);
    }

    /**
     * 1、只要消息抵达Broker就ack=true
     * correlationData：当前消息的唯一关联数据(这个是消息的唯一id)
     * ack：消息是否成功收到
     * cause：失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("rabbit 收到消息");
        log.info("correlationData" + "==>ack:[" + ack + "]==>cause:[" + cause + "]");
    }

}
