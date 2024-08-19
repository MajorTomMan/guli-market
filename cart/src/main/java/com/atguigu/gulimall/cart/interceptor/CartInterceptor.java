/*
 * @Date: 2024-08-10 10:10:40
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-13 00:38:07
 * @FilePath: \Guli\cart\src\main\java\com\atguigu\gulimall\cart\interceptor\CartInterceptor.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.cart.interceptor;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.gulimall.cart.to.UserInfoTo;
import com.atguigu.gulimall.common.constant.AuthServerConstant;
import com.atguigu.gulimall.common.constant.CartConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CartInterceptor implements HandlerInterceptor {
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        HttpSession session = request.getSession();
        LinkedHashMap<String, Object> attribute = (LinkedHashMap) session.getAttribute(AuthServerConstant.LOGIN_USER);
        UserInfoTo userInfoTo = new UserInfoTo();
        if (attribute != null && !attribute.isEmpty()) {
            Integer id = (Integer) attribute.get("id");
            userInfoTo.setUserId(Long.valueOf(id));
        }
        /*
         * // 1 用户已经登录，设置userId
         * if (memberResponseVo != null) {
         * userInfoTo.setUserId(memberResponseVo.getId());
         * }
         */
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("cookie.name->" + cookie.getName());
                log.info("cookie.value->" + cookie.getValue());
                // 2 如果cookie中已经有user-Key，则直接设置
                if (cookie.getName().equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        // 3 如果cookie没有user-key，我们通过uuid生成user-key
        if (!StringUtils.hasText(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            log.info("uuid->" + uuid);
            userInfoTo.setUserKey(uuid);
        }

        // 4 将用户身份认证信息放入threadlocal进行传递
        threadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        // 如果cookie中没有user-key，我们为其生成
        if (!userInfoTo.getTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
