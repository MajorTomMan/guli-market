package com.atguigu.gulimall.order.listener;

import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.PayAsyncVo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RestController
public class OrderPayListener {
    @Autowired
    OrderService orderService;

    @GetMapping("/payed/notify")
    public String handleAliPayed(@RequestBody PayAsyncVo vo) {
        /*
         * 只要接收到支付宝给我们异步的通知,返回success,支付宝就不会再通知
         */
        String result = orderService.handlePayResult(vo);
        return result;
    }
}
