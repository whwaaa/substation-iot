package com.iot.controller;

import com.iot.pojo.Operat;
import com.iot.service.OperatService;
import com.iot.utils.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operat")
public class OperatController {

    @Resource
    private OperatService service;

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
