package com.iot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:devices.properties")
@ConfigurationProperties(prefix="devices")
public class IotDevicesObjConfig {

    private String host;
    private List<Map<String, String>> device;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<Map<String, String>> getDevice() {
        return device;
    }

    public void setDevice(List<Map<String, String>> device) {
        this.device = device;
    }
}
