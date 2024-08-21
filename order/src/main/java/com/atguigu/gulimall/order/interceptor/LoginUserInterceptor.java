package com.atguigu.gulimall.order.interceptor;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.atguigu.gulimall.common.constant.AuthServerConstant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * LoginInterceptor
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<LinkedHashMap<String, Object>> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        HttpSession session = request.getSession();
        LinkedHashMap<String, Object> attribute = (LinkedHashMap) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null && !attribute.isEmpty()) {
            loginUser.set(attribute);
            return true;
        }
        session.setAttribute("msg", "请先登录");
        response.sendRedirect("redirect:http://auth.gulimall.com/login.html");
        return false;
    }
}