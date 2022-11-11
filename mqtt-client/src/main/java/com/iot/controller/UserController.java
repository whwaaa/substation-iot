package com.iot.controller;

import com.iot.pojo.User;
import com.iot.service.UserService;
import com.iot.utils.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService service;

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
        if(add > 0){
            return new AjaxResult();
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

    // 根据主键逻辑删除
    @DeleteMapping(path = "/{id}")
    public AjaxResult deleteById(@PathVariable("id") Long id){
        Integer res = service.deleteById(id);
        if(res > 0){
            return new AjaxResult(204, "ok", null);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }
}

