package com.iot.utils.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ClientMQTT {

    private final String host;
    private final String clientID;
    private final String[] topic;
    private final String user;
    private final String password;

    public ClientMQTT(String clientID, String user, String password, String[] topic, String host) {
        this.clientID = clientID;
        this.user = user;
        this.password = password;
        this.topic = topic;
        this.host = host;
    }

    public void clientStart(){
        try {
            MqttClient client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(10);
            options.setConnectionTimeout(50);
            options.setUserName(user);
            options.setPassword(password.toCharArray());
            client.setCallback(new PublishCallBack());
            client.connect(options);
            int[] Qos = {2};
            client.subscribe(topic,Qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}

