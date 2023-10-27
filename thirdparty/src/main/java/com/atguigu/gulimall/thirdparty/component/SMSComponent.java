package com.atguigu.gulimall.thirdparty.component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;

import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
@Component
public class SMSComponent {
    @Value(value = "${spring.cloud.alicloud.access-id}")
    private String accessId;
    @Value(value = "${spring.cloud.alicloud.access-key}")
    private String accesskey;
    @Value(value = "${spring.cloud.alicloud.sms.endpoint}")
    private String endpoint;
    @Value(value = "${spring.cloud.alicloud.sms.region}")
    private String region;
    @Value(value = "${spring.cloud.alicloud.sms.sign}")
    private String sign;
    @Value(value = "${spring.cloud.alicloud.sms.templatecode}")
    private String templateCode;
    @Autowired
    private Gson gson;
    private Map<String, String> codes = new HashMap<>();

    public void sendSms(String phone, String code) throws InterruptedException, ExecutionException {
        StaticCredentialProvider provider = StaticCredentialProvider.create(
                Credential.builder().accessKeyId(accessId)
                        .accessKeySecret(accesskey).build());
        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region(region) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                                .setConnectTimeout(Duration.ofSeconds(30)))
                .build();
        codes.put("code", code);
        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(sign)
                .templateCode(templateCode)
                .phoneNumbers(phone)
                .templateParam(gson.toJson(codes))
                .build();
        try {
            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
            // Synchronously get the return value of the API request
            SendSmsResponse resp = response.get();
            String result = resp.getBody().getMessage();
            if (result.equals("OK")) {
                log.info("短信发送成功");
            } else {
                log.warn("短信发送失败");
                log.warn("错误原因是:" + result);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
        }
    }
}
