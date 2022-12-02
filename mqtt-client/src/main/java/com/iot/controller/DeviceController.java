package com.iot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.config.ParamConfig;
import com.iot.pojo.Device;
import com.iot.service.DeviceService;
import com.iot.utils.AjaxResult;
import com.iot.vo.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DeviceService service;
    @Resource
    private ParamConfig paramConfig;

    // 根据主键查询
    @GetMapping(path = "/{id}")
    public AjaxResult queryById(@PathVariable Long id) {
        Device device = service.queryById(id);
        if( device != null ){
            return new AjaxResult(device);
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 添加一条数据
    @PostMapping
    public AjaxResult add(Device device){
        Integer add = service.add(device);
        if(add > 0){
            return new AjaxResult();
        }
        return new AjaxResult(500, "服务器内部异常, 请稍后再试!");
    }

    // 根据主键更新
    @PutMapping(path = "{id}")
    public AjaxResult updateById(@PathVariable(value = "id")Long id, Device device){
        Integer res = service.updateById(id, device);
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


//    // 根据主键查询
//    @GetMapping(path = "/state/{clientId}")
//    public AjaxResult queryStateByClientId(@PathVariable String clientId) {
//
//        Long dId = Long.valueOf(clientId.replaceAll("client", ""));
//        Device device = service.queryById(dId);
//        return new AjaxResult(200, "ok", new DeviceState(clientId, "on", null, device.getSettings()));
//    }
    // 根据主键查询
    @GetMapping(path = "/state/{clientId}")
    public AjaxResult queryStateByClientId(@PathVariable String clientId) {

        String url = paramConfig.getUrl();
        String api = "/api/v4/clients/" + clientId;
        //封装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic YWRtaW46cHVibGlj");
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(headers);

        ResponseEntity<String> exchange = restTemplate.exchange(url + api, HttpMethod.GET, formEntity, String.class);
        String body = exchange.getBody();

        JSONArray dataJson = JSONObject.parseObject(body).getJSONArray("data");
        if ( dataJson.size() != 0 ) {
            JSONObject jsonObject = dataJson.getJSONObject(0);
            String connectedTime = jsonObject.getString("connected_at");
            Boolean connected = jsonObject.getBoolean("connected");
//            Date date = null;
            if ( connected && !"".equals(connectedTime) && connectedTime!=null ){
//                try {
//                    date = DateFormatUtils.StringToDate(connectedTime);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                Long dId = Long.valueOf(clientId.replaceAll("client", ""));
                Device device = service.queryById(dId);
                return new AjaxResult(200, "ok", new DeviceState(clientId, "on", connectedTime, device.getSettings()));
            }
        }
        return new AjaxResult(500, "设备"+clientId+"不在线!");
    }

    public static void main(String[] args) {
        Long client = Long.valueOf("client003".replaceAll("client", ""));
        System.out.println(client);
    }
}
