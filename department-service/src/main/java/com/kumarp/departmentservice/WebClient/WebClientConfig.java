package com.kumarp.departmentservice.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@ComponentScan
@Slf4j
public class WebClientConfig {
//    @Bean
//    @LoadBalanced
//    public WebClient.Builder createWebClientObject(){
//        return WebClient.builder ().baseUrl ( "http://employee-service" )
//                .defaultHeader ( "Accept","application/json" );
//    }

    @Bean
    @LoadBalanced
    public WebClient.Builder createWebClientObject(){
        log.debug ( "Create object for WebClient " );
        return WebClient.builder();
    }
}
