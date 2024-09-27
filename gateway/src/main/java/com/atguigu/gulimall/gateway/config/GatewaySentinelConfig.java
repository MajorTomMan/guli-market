package com.atguigu.gulimall.gateway.config;

import org.springframework.context.annotation.Configuration;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.DefaultBlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;

@Configuration
public class GatewaySentinelConfig {
    public GatewaySentinelConfig() {
        GatewayCallbackManager.setBlockHandler(new DefaultBlockRequestHandler());
    }
}
