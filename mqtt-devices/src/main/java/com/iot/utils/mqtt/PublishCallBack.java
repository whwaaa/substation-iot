package com.iot.utils.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.iot.config.IotDevicesObjConfig;
import com.iot.pojo.DeviceMsg;
import com.iot.service.DeviceMsgService;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PublishCallBack implements MqttCallback, ApplicationContextAware {

    private static DeviceMsgService service;
    private static final  Map<String, Long> topicDeviceIdMap = new HashMap<>();
    private static final Map<String, Map<String, String>> clientIdDeviceParamMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(PublishCallBack.class);
    private static String host;
    private static final Map<String, Integer> lostTimesList = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        service = applicationContext.getBean(DeviceMsgService.class);
        List<Map<String, String>> devices = applicationContext.getBean(IotDevicesObjConfig.class).getDevice();
        host = applicationContext.getBean(IotDevicesObjConfig.class).getHost();
        for (Map<String, String> device : devices) {
            new ClientMQTT(
                    device.get("clientId")
                    , device.get("user")
                    , device.get("password")
                    , new String[]{device.get("topic")}
                    , host).clientStart();
            topicDeviceIdMap.put(device.get("topic"), Long.valueOf(device.get("uId")));
            clientIdDeviceParamMap.put(device.get("clientId"), device);
            lostTimesList.put(device.get("clientId"), 0);
        }
    }

    // 连接断掉会执行到这里
    public void connectionLost(Throwable throwable) {
        logger.error("连接断开: " + throwable.toString());
        // 遍历所有qmttClient
        for (MqttClient mqttClient : ClientMQTT.mqttClientList) {
            String clientId = mqttClient.getClientId();
            // 找出断开连接的client
            if ( !mqttClient.isConnected() ) {

                // 连续断线5次以上则休眠重连
                int lostTimes = lostTimesList.get(clientId) + 1;
                if ( lostTimes >= 5 ) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 先将client从列表中删除,列表存放在线的client
                ClientMQTT.mqttClientList.remove(mqttClient);
                // 通过clientId查询出原始参数
                Map<String, String> deviceParam = clientIdDeviceParamMap.get(clientId);
                // 重新创建连接
                new ClientMQTT(
                        deviceParam.get("clientId")
                        , deviceParam.get("user")
                        , deviceParam.get("password")
                        , new String[]{deviceParam.get("topic")}
                        , host).clientStart();
            } else {
                // 清除在线设备的重连次数
                lostTimesList.put(clientId, 0);
            }
        }
    }

    // subscribe后会执行到这里
    public void messageArrived(String s, MqttMessage mqttMessage) {
        Long dId = topicDeviceIdMap.get(s);
        //message格式: {"state":"1","uid":"0"}
        Integer state;
        Long uId;
        try {
            String message = new String(mqttMessage.getPayload());
            JSONObject object = JSONObject.parseObject(message);
            state = object.getInteger("state");
            uId = Long.valueOf(object.getInteger("uid"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("JsonParse_ERR: topic:" + s + " Mqtt消息解析Json失败!");
            return;
        }


        DeviceMsg deviceMsg = new DeviceMsg();
        deviceMsg.setDid(dId);
        deviceMsg.setState(state);
        deviceMsg.setUid(uId);
        Integer res = service.add(deviceMsg);
        if ( res == 0 ) {
            logger.error("MySql_ERR: topic:" + s + " dId:" + dId + " uId:" + uId + " state:" + state +" 数据库存入失败!");
        }
    }

    // publish可以执行到这里
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //System.out.println("This is deliveryComplete method----->"+iMqttDeliveryToken.isComplete());
    }

}

