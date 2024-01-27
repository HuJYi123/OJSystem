package com.ayi.ayiojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ayi.ayiojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.ayi")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ayi.ayiojbackendserviceclient.service"})
public class AyiojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AyiojBackendQuestionServiceApplication.class, args);
    }

}
