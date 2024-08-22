package com.atguigu.gulimall.common.interceptor;

import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // TODO Auto-generated method stub

        // 为了解决feign发请求时没有cookie
        // 导致其他微服务认为没有登录的问题而将浏览器请求中的cookie附加到feign请求中
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes != null) {
            Cookie[] cookies = requestAttributes.getRequest().getCookies();
            String finalCookie = Stream.of(cookies).map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining(";"));
            if (StringUtils.hasText(finalCookie)) {
                template.header("Cookie", finalCookie);
            }
        }

    }

}
