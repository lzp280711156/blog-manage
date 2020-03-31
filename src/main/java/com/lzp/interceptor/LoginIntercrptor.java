package com.lzp.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器
 */
public class LoginIntercrptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断用户是否登陆
        if (request.getSession().getAttribute("user") == null) {
            // 未登录，重定向至登陆页面
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
