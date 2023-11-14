package com.kumarp.kafkaservice.consumer;

import com.kumarp.kafkaservice.payload.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger ( KafkaJsonConsumer.class );
    @KafkaListener(topics="${spring.kafka.topic-json.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(User user){
        LOGGER.info(String.format ( "Json message received --> %s", user.toString () ));
    }
}
