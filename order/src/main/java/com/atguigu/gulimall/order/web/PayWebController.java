package com.atguigu.gulimall.order.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.atguigu.gulimall.order.config.AlipayConfig;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.PayVo;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class PayWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    @ResponseBody
    public String payOrder(@RequestParam("orderSn") String orderSn) {
        // TODO: process POST request
        PayVo payVo = orderService.getOrderPay(orderSn);
        try {
            String pay = AlipayConfig.pay(payVo);
            if (StringUtils.hasText(pay)) {
                return pay;
            } else {
                return null;
            }
        } catch (AlipayApiException e) {

            log.error("支付宝支付报错");
            log.error(e.getMessage());
            return null;
        }
    }

}
