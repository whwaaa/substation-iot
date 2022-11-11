package com.iot.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GlobalInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //logger.debug("处理器执行前执行!");
        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin",origin);
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        // 额外暴露自定义的首部
        response.setHeader("Access-Control-Expose-Headers","REDIRECT,TOKEN_MSG,CONTENTPATH,jwt");
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()){
//            String s = headerNames.nextElement();
//            System.out.println(s + " : " + request.getHeader(s));
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //logger.debug("处理器执行后执行!");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.debug("跳转前执行!");
    }
}
