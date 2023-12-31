package com.kumarp.departmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class DepartmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run ( DepartmentServiceApplication.class, args );
    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate createRestTemplateObject() {
//        return new RestTemplate();
//    }
//    @Bean
//    public ServerCodecConfigurer serverCodecConfigurer() {
//        return ServerCodecConfigurer.create();
//    }
}
