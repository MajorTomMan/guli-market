package com.atguigu.gulimall.order.listener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

/* 
 *  加上ACKMODE避免
 * com.rabbitmq.client.ShutdownSignalException: 
 * channel error; protocol method: 
 * #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED
 *  - unknown delivery tag 1, class-id=60, method-id=80)
 */
@Service
@Log4j2
@RabbitListener(queues = { "order.release.order.queue" })
public class OrderCloseListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void closeListener(OrderEntity entity, Channel channel, Message message) throws IOException {
        // 关单逻辑
        try {
            log.info("准备关单");
            orderService.closeOrder(entity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // TODO: handle exception
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
