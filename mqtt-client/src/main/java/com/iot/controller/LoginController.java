package com.iot.controller;

import com.iot.pojo.DeviceMsg;
import com.iot.pojo.User;
import com.iot.service.UserService;
import com.iot.utils.AjaxResult;
import com.iot.vo.LoginSuccess;
import com.iot.vo.UserPasswordMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping
    public AjaxResult login(@RequestParam(name = "username") String username,
                            @RequestParam(name = "password") String password,
                            HttpServletRequest request,
                            HttpServletResponse response){
        if ( username!=null && !"".equals(username.trim())
                && password!=null && !"".equals(password.trim()) ) {
            List<User> users = userService.queryUsernameAndPassword(username.trim(), password.trim());
            if ( users.size() == 1 && users.get(0).getIsDelete() == 0 ) {// 账号已授权
                // username : 当前时间戳
                String time = String.valueOf(System.currentTimeMillis());
                UserPasswordMap.userPasswdMap.put(username, time);
                return new AjaxResult(200, "ok", new LoginSuccess(username, time, users.get(0)));
            }
        }
        return new AjaxResult(401, "用户名或密码错误!");
    }
}
