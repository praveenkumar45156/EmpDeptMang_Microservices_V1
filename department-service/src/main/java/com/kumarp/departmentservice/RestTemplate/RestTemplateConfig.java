package com.kumarp.departmentservice.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ComponentScan
@Slf4j
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate createRestTemplateObject() {
        log.debug ( "Create RestTemplate Bean" );
        return new RestTemplate();
    }
}
