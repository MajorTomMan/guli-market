package com.atguigu.gulimall.order.config;

import java.util.HashMap;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.atguigu.gulimall.order.vo.PayVo;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AlipayConfig {
    private static String APP_ID = "9021000140659914";
    private static String APP_PRIVATE_KEY = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDNhMzGRwv4vuww2ks7BOpAk2fPc3x5SL87Co5FTZoFMBBCnt9DFrQmhlFtp6SnnjzyAcaySbInjl3sK/FlAq5rYwDNF/TuTnEvwLbxt2qtfWZr+6Oz2ksOR41nHz2amlFBrU88MJA+XtDC/bd40CmVYKxFK3zXFvfJrlxdX5SLU0ZyVGMP17LQ6MJ4+CCSoLRnXh0A67JEYbQghcGJcT28YX7PVLlI3GF93jQIAVNLW0ZfGmnm9kkSFbEw4pzUgUL7dqTcxdo9Up4QIVMlmkmUz5qGTFCufyoqBHba51GeN/t3NidX7LQ1/e26FX4XhP/akzr7WO4vsO+x3n4N36sZAgMBAAECggEBAK05nPwW0DFmg2Ad7sEdQHETjbFejvEPbxfNquMhKIvs2he9H0PF9CxwlFvPrMoXZxk4hAisczxCFEpHM51HKEqdVQSJh8oBVgfA8LJGPeVjSBwGKxIiEZdEmhvsShlwrsKj2KBL8piPhHiGL8nl8tbUd/+JSx1kX5CfpXySXnsbQB0XjS8MxZnjy+/+LfdVECI21l04h12Li/IxuuQdd2+knzECE6CByVRYXiQjado5cu5BOVIFu+AZPi19wfr3u9yLzmpamRrA2T8kvtbOzqBDlAmU2y5QIR9GNYtuDyY10Wz5QmQPM9AIPjAl3Uf7RSkQEubU5Rf5w/EQJQ+MqIECgYEA5rWxb0Zmjfmxd546oX3PFeHZ935Rns1bW1fSEwXOgyq+g6H17hdU1xX16sajS3JhMFj+WVpqQvMzyBuJI5u4AXYoXzBn1sF9C06XEKdwW+fhmPkeEvJmMoG3Rr7eVFaaE7rqGmm8scMRIiyHYSyrLp21Ai5JiKmlhiNBqIe0WjECgYEA5Awu2+943J/Rrw+BzQGqKOPGiU6lYZr3C94xcMjc3pd1CoJyjniEc0QD6gsyolUiyhALilJQvbV0K8wCpB6BeXKMFgfmvrnds7dIv3KhYNS3E+Jr+Pkoto5Y6MAmi4D6HiFqqekDRjPvEdFpcFxP/nowfyWpY5UX2MnMWZ65PWkCgYEAtbXxlLUcXUKHFrHXeA6BJrkGVjp1yt/vzpfy6+ys5P6roz3TV72kouu3Dz14rBnglpXhbzsZarXm2PtnHlatrOkFj0Ou5aTr+hXiQcm/Y4PVWgkMONwWxj0Iv33h2weCv3t+m2Mgrdn4CrSLxGvMQCESyi7u1+bWajomoriPwmECgYEAj0IgBcYQNPipLsIdFn4fOQSt6f/f4MsKuwfgIyvrpGGdAvcjo+CNxCNMGjDipBu7eRhPR5uiYSqmSgUk+5i2WWbeHdDk4Ze9cHKJXstSGZRHVlR847ESJGyT6dMb47MobNktOoPIrMdfPeBBznYPQpvb72PnX0l/644Js5sEX1ECgYEAzzMXvG0eMs/LaPu0dzzNoweyXBxFnnYty8+azDgt53N/DLn04F/pxREzznKrcfUShfsuFV5jA424q99qvBu47A9Amuh8sLE2LVE8YusmL8ndFDeniU03KjJonXFov6n4kU8jW6KEHIt62kOvMjaYFQz3Limb74exHHMwhosAOQs=";
    private static String CHARSET = "UTF-8";
    private static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgUnftVfRNQ/JKW91OgLPBh3s7p1Z87EIbZry0oXuy+yBTzJA4wdMf/huFMMiFUxbtTDL8JjuR/r7Dqi4DfJH3ua9opiuCArPL1gisW0hAJkVhzeyrTNDRvRG0XKeik2pegOfHLgbQSCdqZGOZ19NyvMobPUiGuC+Zx92uiYuIleI6rqyy5fJoGdT1xjZiRCZoH4nsYn3vogXLstnS0Hw4WVDyb8NxLL4mUS8C5gCcVsEZmzsGbV/G723/DketSXt4cKzWMq+QyKWSuOqbWtE6MkwhIWAQ3ES72Sc7Eoi+YwrggquFEqTRUXPNfM9aFy6TL4MeGdhxNnejA7eS7lJqwIDAQAB";
    private static String NOTIFY_URL = "http://member.gulimall.com/memberOrder.html";
    private static String RETURN_URL = "http://member.gulimall.com/memberOrder.html";

    public static String pay(PayVo vo) throws AlipayApiException {
        Gson gson = new Gson();
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi-sandbox.dl.alipaydev.com/gateway.do",
                APP_ID,
                APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.page.pay
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数
        // 此次只是参数展示，未进0行字符串转义，实际情况下请转义
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("out_trade_no", vo.getOut_trade_no());
        hashMap.put("total_amount", vo.getTotal_amount());
        hashMap.put("subject", vo.getSubject());
        hashMap.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(gson.toJson(hashMap));
        request.setNotifyUrl(NOTIFY_URL);
        request.setReturnUrl(RETURN_URL);
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        // 调用成功，则处理业务逻辑
        if (response.isSuccess()) {
            // .....
            return response.getBody();
        } else {
            log.error("支付宝付款失败");
            log.error("" + response.getBody());
            return null;
        }
    }
}
