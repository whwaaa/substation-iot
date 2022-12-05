package com.iot.controller;

import com.iot.pojo.Operat;
import com.iot.pojo.User;
import com.iot.service.DeviceService;
import com.iot.service.OperatService;
import com.iot.service.UserService;
import com.iot.utils.AjaxResult;
import com.iot.utils.mqtt.ClientMQTT;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/operate")
public class OperateController {

    @Resource
    private OperatService service;
    @Resource
    private DeviceService deviceService;
    @Resource
    private UserService userService;

    public static Map<String, Thread> operateMap= new HashMap<>();

    // 根据主键更新
    @PutMapping(path = "/peidianshi")
    public AjaxResult peidianshiOperate(@PathParam(value = "dId") Long dId,
                                        @PathParam(value = "topic") String topic,
                                        @PathParam(value = "state") String state,//1关 2开
                                        HttpServletRequest request){
        String account = null;
        for (Cookie cookie : request.getCookies()) {
            if ("iot-username".equals(cookie.getName())){
                account = cookie.getValue();
            }
        }
        User user = userService.queryByAccount(account);
        String cmd = "{\"state\":" + state + ",\"uid\":" + user.getId() + "}";
        ClientMQTT.sendMessage(topic, cmd);

        Thread thread = Thread.currentThread();
        //key: uId_dId_peidianshi
        operateMap.put(user.getId() + "_" + dId + "_peidianshi", thread);
        thread.interrupt();

//        Integer res = service.peidianshiOperate(id, operat);
//        if(res != 0){
//            return new AjaxResult(200,"更新成功");
//        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }


    // 根据主键查询
    @GetMapping(path = "{id}")
    public AjaxResult queryById(@PathVariable Long id) {
        Operat operat = service.queryById(id);
        if( operat != null ){
            return new AjaxResult(operat);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 添加一条数据
    @PostMapping
    public AjaxResult add(Operat operat){
        Integer add = service.add(operat);
        if(add > 0){
            return new AjaxResult();
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键更新
    @PutMapping(path = "{id}")
    public AjaxResult updateById(@PathVariable(value = "id")Long id, Operat operat){
        Integer res = service.updateById(id, operat);
        if(res != 0){
            return new AjaxResult(200,"更新成功");
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键删除
    @DeleteMapping(path = "{id}")
    public AjaxResult deleteById(@PathVariable("id") Long id){
        Integer res = service.deleteById(id);
        if(res > 0){
            return new AjaxResult(204, "ok", null);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }
}
