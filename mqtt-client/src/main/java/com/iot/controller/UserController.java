package com.iot.controller;

import com.iot.pojo.User;
import com.iot.service.UserService;
import com.iot.utils.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService service;

    // 查询未授权的账号
    @GetMapping(path = "/isdeletelist")
    public AjaxResult queryIsDelete() {
        List<User> users = service.queryIsDeleteList();
        if( users.size() != 0 ){
            return new AjaxResult(200, "ok", users);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }


    // 查询重置密码的账号
    @GetMapping(path = "/resetpasswd")
    public AjaxResult queryResetPasswd() {
        List<User> users = service.queryResetPasswd();
        if( users.size() != 0 ){
            return new AjaxResult(200, "ok", users);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }



    // 根据主键查询
    @GetMapping(path = "/{id}")
    public AjaxResult queryById(@PathVariable Long id) {
        User user = service.queryById(id);
        if( user != null ){
            return new AjaxResult(user);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 添加一条数据
    @PostMapping
    public AjaxResult add(User user){
        Integer add = service.add(user);
        if( add!=null && add>0){
            return new AjaxResult(201, "ok");
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键更新
    @PutMapping(path = "{id}")
    public AjaxResult updateById(@PathVariable(value = "id")Long id, User user){
        Integer res = service.updateById(id, user);
        if(res != 0){
            return new AjaxResult(200,"更新成功");
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 授权账号
    @PutMapping(path = "/auth")
    public AjaxResult authAccount(@PathParam(value = "account") String account){
        if ( account!=null && !"".equals(account) ) {
            Integer res = service.authAccount(account);
            if(res != 0){
                return new AjaxResult(200,"授权成功!");
            }
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }


    // 请求密码重置
    @PutMapping(path = "/requestreset")
    public AjaxResult requestResetAccount(@PathParam(value = "account") String account){
        if ( account!=null && !"".equals(account) ) {
            Integer res = service.requestResetAccount(account);
            if(res != 0){
                return new AjaxResult(201,"发起重置成功！请联系管理员审核。");
            }
        }
        return new AjaxResult(500, "请求失败, 请检查账号是否正确!");
    }

    // 撤销密码重置
    @PutMapping(path = "/unreset")
    public AjaxResult unResetAccount(@PathParam(value = "account") String account){
        if ( account!=null && !"".equals(account) ) {
            Integer res = service.unResetAccount(account);
            if(res != 0){
                return new AjaxResult(200,"已撤销");
            }
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 重置密码
    @PutMapping(path = "/reset")
    public AjaxResult resetAccount(@PathParam(value = "account") String account){
        if ( account!=null && !"".equals(account) ) {
            Integer res = service.resetAccount(account);
            if(res != 0){
                return new AjaxResult(200,"重置成功，新密码为：123456");
            }
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键删除
    @DeleteMapping(path = "/{id}")
    public AjaxResult deleteById(@PathVariable("id") Long id){
        Integer res = service.deleteById(id);
        if(res > 0){
            return new AjaxResult(204, "ok", null);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 修改密码
    @PutMapping(path = "/modify")
    public AjaxResult modifyPasswd( @PathParam(value = "account") String account
            , @PathParam(value = "oldPasswd") String oldPasswd
            , @PathParam(value = "newPasswd") String newPasswd){
        if ( account!=null && !"".equals(account.trim())
                && oldPasswd!=null && !"".equals(oldPasswd.trim())
                && newPasswd!=null && !"".equals(newPasswd.trim()) ) {
            List<User> users = service.queryUsernameAndPassword(account, oldPasswd);
            if (users.size() == 1) {
                User user = users.get(0);
                user.setPasswd(newPasswd);
                Integer res = service.updateById(user.getId(), user);
                if (res > 0)
                    return new AjaxResult(200, "修改成功。");
            }
        }
        return new AjaxResult(401, "旧密码错误，修改失败。");
    }
}

