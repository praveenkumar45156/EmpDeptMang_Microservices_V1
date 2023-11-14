package com.kumarp.kafkaservice.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaStringProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger ( KafkaStringProducer.class );
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String stringTopicDemo;
    private KafkaStringProducer(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String message){
        LOGGER.info("Message Sent %s", message);
        kafkaTemplate.send ( stringTopicDemo,message );
    }
}
