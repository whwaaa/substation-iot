package com.iot.utils.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public class ClientMQTT {

    public static final String HOST = "tcp://192.168.0.104:1883";
    private static MqttClient client;
    private String clientID;
    private String[] topic;
    private MqttConnectOptions options;
    private String user;
    private String password;



    public ClientMQTT(String clientID, String user, String password, String[] topic) {
        this.clientID = clientID;
        this.user = user;
        this.password = password;
        this.topic = topic;
    }

    public void clientStart(){
        try {
            client = new MqttClient(HOST, clientID, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(10);
            options.setConnectionTimeout(50);
            options.setUserName(user);
            options.setPassword(password.toCharArray());
            client.setCallback(new PublishCallBack());
            //MqttTopic topic = client.getTopic(topicStr);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            //options.setWill(topic,"close".getBytes(),1,true);
            client.connect(options);
            int[] Qos = {2,2,2};
            client.subscribe(topic,Qos);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String topic, String payload){
        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());
        try {
            client.publish(topic,mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}

