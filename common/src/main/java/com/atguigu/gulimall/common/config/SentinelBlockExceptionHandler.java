package com.atguigu.gulimall.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.gulimall.common.exception.BizCodeEmum;
import com.atguigu.gulimall.common.utils.R;
import com.google.gson.Gson;
import com.rabbitmq.client.BlockedCallback;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {
    @Autowired
    Gson gson;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String resourceName, BlockException e)
            throws Exception {
        // TODO Auto-generated method stub
        R error = R.error(BizCodeEmum.TO_MANY_REQUEST.getCode(), BizCodeEmum.TO_MANY_REQUEST.getMsg());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(error));
    }

}
