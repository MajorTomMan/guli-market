package com.atguigu.gulimall.seckill.interceptor;

import java.io.PrintWriter;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
    public static ThreadLocal<LinkedHashMap<String, Object>> loginUser = new InheritableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /*
         * 解决以下异常
         * jakarta.servlet.ServletException:
         * Request processing failed: java.lang.IllegalStateException:
         * Cannot create a session after the response has been committed
         */
        if (response.isCommitted()) {
            return false;
        }
        /*
         * 解决远程请求被拦截的问题
         */
        String requestURI = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/kill", requestURI);
        if (match) {
            /* 解决Feign没有带请求cookie导致其他微服务认为没有登录的问题 */
            HttpSession session = request.getSession();
            LinkedHashMap<String, Object> attribute = (LinkedHashMap) session
                    .getAttribute(AuthServerConstant.LOGIN_USER);
            if (attribute != null && !attribute.isEmpty()) {
                loginUser.set(attribute);
                return true;
            } else {
                // 未登录，返回登录页面
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println(
                        "<script>alert('请先进行登录，再进行后续操作！');location.href='http://auth.gulimall.com/login.html'</script>");
                // session.setAttribute("msg", "请先进行登录");
                // response.sendRedirect("http://auth.gulimall.com/login.html");
                return false;
            }
        }
        return true;
    }
}