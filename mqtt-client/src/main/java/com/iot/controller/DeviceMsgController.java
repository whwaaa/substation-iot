package com.iot.controller;

import com.github.pagehelper.PageInfo;
import com.iot.pojo.DeviceMsg;
import com.iot.service.DeviceMsgService;
import com.iot.utils.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/devicemsg")
public class DeviceMsgController {

    @Resource
    private DeviceMsgService service;

    // 根据主键查询
    @GetMapping(path = "{id}")
    public AjaxResult queryById(@PathVariable Long id) {
        DeviceMsg deviceMsg = service.queryById(id);
        if( deviceMsg != null ){
            return new AjaxResult(deviceMsg);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 添加一条数据
    @PostMapping
    public AjaxResult add(DeviceMsg deviceMsg){
        Integer add = service.add(deviceMsg);
        if(add > 0){
            return new AjaxResult();
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键更新
    @PutMapping(path = "{id}")
    public AjaxResult updateById(@PathVariable(value = "id")Long id, DeviceMsg deviceMsg){
        Integer res = service.updateById(id, deviceMsg);
        if(res != 0){
            return new AjaxResult(200,"更新成功");
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

    // 根据dId查询最后一条状态
    @GetMapping(path = "/latest/{dId}")
    public AjaxResult queryLatestBydId(@PathVariable Long dId) {
        DeviceMsg deviceMsg = service.queryLatestBydId(dId);
        if ( deviceMsg != null ) {
            // '1:开启 2:关闭'
            return new AjaxResult(200, "ok", deviceMsg);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }
}
