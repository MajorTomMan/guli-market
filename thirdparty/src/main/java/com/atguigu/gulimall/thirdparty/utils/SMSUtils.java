
package com.atguigu.gulimall.thirdparty.utils;

import org.springframework.beans.factory.annotation.Value;

public class SMSUtils {
    @Value(value = "${spring.cloud.alicloud.access-id}")
    String accessId;
    @Value(value = "${spring.cloud.alicloud.access-key}")
    String accesskey;
    public void sendSms(){
        
    }
}
