package com.iot.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.vo.UserPasswordMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        && UserPasswordMap.userPasswdMap.get(username).get("time").equals(time)) {

//            //信息更新
//            if ( UserPasswordMap.userPasswdMap.get(username).get("update").equals("true") ) {
//                response.setHeader("REDIRECT", "REDIRECT");
//                response.setHeader("TOKEN_MSG", "relogin");//无权控制配电室设备
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 添加403状态码 (服务器拒绝)
//                return false;
//            }

            //权限校验
            String settings = UserPasswordMap.userPasswdMap.get(username).get("settings");
            JSONObject jsonObject = JSONObject.parseObject(settings);
            JSONObject authArea = jsonObject.getJSONObject("authArea");
            Integer peidianshi = authArea.getInteger("peidianshi");
            String requestURI = request.getRequestURI();
            if ( peidianshi == -1 ) {
                if ( "/device/state/client001".equals(requestURI)
                        || "/device/state/client002".equals(requestURI)
                        || "/device/state/client003".equals(requestURI) ) {
                    response.setHeader("REDIRECT", "REDIRECT");
                    response.setHeader("TOKEN_MSG", "noauth-monitor");//无权监视配电室设备
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 添加403状态码 (服务器拒绝)
                    return false;
                }
            } else if ( peidianshi == 0 ) {
                if ( "/device/1".equals(requestURI)
                        || "/device/2".equals(requestURI)
                        || "/device/3".equals(requestURI) ) {
                    response.setHeader("REDIRECT", "REDIRECT");
                    response.setHeader("TOKEN_MSG", "noauth-control");//无权控制配电室设备
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 添加403状态码 (服务器拒绝)
                    return false;
                }
            }

//            switch ()

//            Cookie cookieUsername = new Cookie("iot-username", username);
//            cookieUsername.setPath("/");
//            cookieUsername.setMaxAge(60*60*24*365);
//            response.addCookie(cookieUsername);
//
//            Cookie cookieTime = new Cookie("iot-time", time);
//            cookieTime.setPath("/");
//            cookieTime.setMaxAge(60*60*24*365);
//            response.addCookie(cookieTime);
//
//            Cookie cookieName = new Cookie("iot-name", name);
//            cookieTime.setPath("/");
//            cookieTime.setMaxAge(60*60*24*365);
//            response.addCookie(cookieName);
//
//            Cookie cookieSet = new Cookie("iot-settings", name);
//            cookieSet.setPath("/");
//            cookieSet.setMaxAge(60*60*24*365);
//            response.addCookie(cookieSet);
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
