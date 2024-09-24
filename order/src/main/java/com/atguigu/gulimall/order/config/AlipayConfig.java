package com.atguigu.gulimall.order.config;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
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
    private static String NOTIFY_URL = "https://3136c225.r8.cpolar.top/payed/notify";
    private static String RETURN_URL = "https://3136c225.r8.cpolar.top/payed/return";

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

    /*
     * 1. 在进行异步通知交互时，如果支付宝收到的应答不是 success
     * ，支付宝会认为通知失败，会通过一定的策略定期重新发起通知。通知的间隔频率为：4m、10m、10m、1h、2h、6h、15h。
     * 2. 商家设置的异步地址（notify_url）需保证无任何字符，如空格、HTML 标签，且不能重定向。（如果重定向，支付宝会收不到 success
     * 字符，会被支付宝服务器判定为该页面程序运行出现异常，而重发处理结果通知）
     * 3. 支付宝是用 POST
     * 方式发送通知信息，商户获取参数的方式如下：request.Form("out_trade_no")、$_POST['out_trade_no']。
     * 4. 支付宝针对同一条异步通知重试时，异步通知参数中的 notify_id 是不变的。
     * 
     */
    /*
     * ------------------------------如何验证签名-----------------------------------------
     * -------------------------------------------------------------------
     */
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
    /* ------------------------------------------------------------------------ */
    public static boolean check(Map<String, String> params) {
        String charset = params.get("charset");
        String sign_type = params.get("sign_type");
        // 验证签名
        try {
            boolean isPass = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, charset, sign_type);
            return isPass;
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            log.error("支付宝验证签名出错");
            log.error(e.getErrMsg());
            log.error(e.getErrCode());
            return false;
        }
    }
}
