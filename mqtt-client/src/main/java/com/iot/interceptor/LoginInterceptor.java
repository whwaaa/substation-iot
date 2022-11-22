package com.iot.interceptor;

import com.iot.vo.UserPasswordMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //HttpSession session = request.getSession();
        //Boolean isLogin = (Boolean)session.getAttribute("isLogin");
        String username = null;
        String time = null;
        String name = null;
        Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if ("iot-username".equals(cookieName)) username = cookie.getValue();
                if ("iot-time".equals(cookieName)) time = cookie.getValue();
                if ("iot-name".equals(cookieName)) name = cookie.getValue();
            }
        }
        if (UserPasswordMap.userPasswdMap.get(username) != null
        && UserPasswordMap.userPasswdMap.get(username).equals(time)) {
            Cookie cookieUsername = new Cookie("iot-username", username);
            cookieUsername.setPath("/");
            cookieUsername.setMaxAge(60*60*24*7);
            response.addCookie(cookieUsername);

            Cookie cookieTime = new Cookie("iot-time", time);
            cookieTime.setPath("/");
            cookieTime.setMaxAge(60*60*24*7);
            response.addCookie(cookieTime);

            Cookie cookieName = new Cookie("iot-name", name);
            cookieTime.setPath("/");
            cookieTime.setMaxAge(60*60*24*7);
            response.addCookie(cookieName);
            return true;

        } else {
            // 返回重定向head标志给前端ajax
            response.setHeader("REDIRECT", "REDIRECT");
            response.setHeader("TOKEN_MSG", "no-token");
            // 返回重定向路径给前端ajax
            response.setHeader("CONTENTPATH", "/index/login.html");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 添加403状态码 (服务器拒绝)
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
