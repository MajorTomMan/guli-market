package com.atguigu.gulimall.order.listener;

import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gulimall.order.config.AlipayConfig;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.PayAsyncVo;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
@RequestMapping
public class OrderPayListener {
    @Autowired
    OrderService orderService;

    // 日期时间格式
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/payed/notify")
    @ResponseBody
    public String handleAliPayedNotify(@RequestParam Map<String, String> map) {
        log.info("收到支付宝异步通知");
        map.forEach((k, v) -> {
            log.info("k->" + k + " v->" + v);
        });
        boolean check = AlipayConfig.check(map);
        if (check) {
            /*
             * 只要接收到支付宝给我们异步的通知并且验证签名通过,返回success,支付宝就不会再通知
             */
            PayAsyncVo vo = mapToBean(map, PayAsyncVo.class);
            String result = orderService.handlePayResult(vo);
            return result;
        } else {
            return "failed";
        }

    }

    @GetMapping("/payed/return")
    public String handleAliPayedReturn(@RequestParam Map<String, String> param) {
        log.info("收到支付宝同步通知请求");
        param.forEach((k, v) -> {
            log.info("k->" + k + " v->" + v);
        });
        return "redirect:http://member.gulimall.com/memberOrder.html";
    }

    /**
     * 将 Map 转换为 Java Bean。
     * 
     * @param map       要转换的 Map
     * @param beanClass 目标 Bean 的类
     * @param <T>       目标 Bean 类型
     * @return 转换后的 Bean 对象
     */
    public static <T> T mapToBean(Map<String, String> map, Class<T> beanClass) {
        T bean = BeanUtils.instantiateClass(beanClass);
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

        map.forEach((propertyName, propertyValue) -> {
            if (beanWrapper.isWritableProperty(propertyName)) {
                if (propertyValue instanceof String && propertyName.toLowerCase().contains("_time")) {
                    // 如果字段名包含 _time 且值为 String 类型，则尝试转换为 Date 类型
                    Date dateValue = parseDate((String) propertyValue);
                    beanWrapper.setPropertyValue(propertyName, dateValue);
                } else {
                    beanWrapper.setPropertyValue(propertyName, propertyValue);
                }
            } else {
                System.out.println("Property '" + propertyName + "' does not exist, skipping...");
            }
        });

        return bean;
    }

    /**
     * 将字符串转换为 Date 对象。
     * 
     * @param dateString 要转换的日期时间字符串
     * @return 解析后的 Date 对象
     */
    private static Date parseDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date: " + dateString, e);
        }
    }
}
