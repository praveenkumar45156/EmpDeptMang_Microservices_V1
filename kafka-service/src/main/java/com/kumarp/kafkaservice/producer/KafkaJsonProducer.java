package com.kumarp.kafkaservice.producer;

import com.kumarp.kafkaservice.payload.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaJsonProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger ( KafkaJsonProducer.class );

    @Value("${spring.kafka.topic-json.name}")
    private String jsonTopicDemo;
    private KafkaTemplate<String, User> kafkaTemplate;
    private KafkaJsonProducer(KafkaTemplate<String,User> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(User data){
        LOGGER.info("Message Sent --> %s", data.toString ());
        Message<User> message = MessageBuilder.withPayload ( data ).setHeader( KafkaHeaders.TOPIC,"json_topic_demo" ).build ();
        kafkaTemplate.send ( message );
    }
}
