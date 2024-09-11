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
        /*
         * 验证签名过程参考
         * https://opendocs.alipay.com/open/270/105902#%E5%BC%82%E6%AD%A5%E8%BF%94%E5%9B
         * %9E%E7%BB%93%E6%9E%9C%E7%9A%84%E9%AA%8C%E7%AD%BE
         */
        /*
         * 1.在通知返回参数列表中，除去 sign、sign_type 两个参数外，凡是通知返回回来的参数皆是待验签的参数。
         * 2.将剩下参数进行 url_decode，然后进行字典排序，组成字符串，得到待签名字符串
         * 3.将签名参数（sign）使用 base64 解码为字节码串。
         * 4.使用 RSA 的验签方法，通过签名字符串、签名参数（经过 base64 解码）及支付宝公钥验证签名。
         */
        /*
         * 5.需要严格按照如下描述校验通知数据的正确性：
         * 1. 商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
         * 2. 判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
         * 3. 校验通知中的 seller_id（或者 seller_email）是否为 out_trade_no
         * 这笔单据的对应的操作方（有的时候，一个商家可能有多个 seller_id/seller_email）。
         * 4. 验证 app_id 是否为该商家本身。
         */
        /*
         * 上述 1、2、3、4
         * 有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。在上述验证通过后商家必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，
         * 并且过滤重复的通知结果数据。在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED
         * 时，支付宝才会认定为买家付款成功。
         * 注意：
         * ● 状态 TRADE_SUCCESS 的通知触发条件是商家开通的产品支持退款功能的前提下，买家付款成功。
         * ● 交易状态 TRADE_FINISHED
         * 的通知触发条件是商家开通的产品不支持退款功能的前提下，买家付款成功；或者，商家开通的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限。
         */
        // 验证签名
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
