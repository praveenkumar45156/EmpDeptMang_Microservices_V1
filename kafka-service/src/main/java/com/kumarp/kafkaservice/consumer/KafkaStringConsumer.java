package com.kumarp.kafkaservice.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaStringConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger ( KafkaStringConsumer.class );
    @KafkaListener(topics="${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message){
        LOGGER.info(String.format ( "Message received --> %s", message ));
    }
}
