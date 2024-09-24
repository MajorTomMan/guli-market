package com.atguigu.gulimall.order.listener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.atguigu.gulimall.common.to.mq.SeckillOrderTo;
import com.atguigu.gulimall.order.service.OrderService;
import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RabbitListener(queues = "order.seckill.order.queue")
@Service
public class OrderSecKillListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void secKillListener(SeckillOrderTo to, Channel channel, Message message) throws IOException {
        try {
            log.info("准备创建秒杀单");
            orderService.createSecKillOrder(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // TODO: handle exception
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
