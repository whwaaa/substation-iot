package com.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.iot.mapper")
@SpringBootApplication
//@EnableDiscoveryClient // 开启Eureka客户端发现功能
public class MqttClientApplication{
    public static void main(String[] args) {
        SpringApplication.run(MqttClientApplication.class, args);
    }
}
