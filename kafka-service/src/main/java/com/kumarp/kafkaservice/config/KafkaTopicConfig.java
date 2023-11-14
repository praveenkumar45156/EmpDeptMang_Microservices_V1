package com.kumarp.kafkaservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.name}")
    private String stringTopicDemo;
    @Value("${spring.kafka.topic-json.name}")
    private String jsonTopicDemo;
    @Bean
    public NewTopic stringDemoTopics(){
        return TopicBuilder.name ( stringTopicDemo).build ();
    }

    @Bean
    public NewTopic jsonDemoTopics(){
        return TopicBuilder.name ( jsonTopicDemo ).build ();
    }
}
