package com.kumarp.kafkaservice.controller;

import com.kumarp.kafkaservice.payload.User;
import com.kumarp.kafkaservice.producer.KafkaJsonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json/kafka")
public class MessageJsonController {

    @Autowired
    private KafkaJsonProducer kafkaJsonProducer;

    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody User user){
        kafkaJsonProducer.sendMessage ( user );
        return ResponseEntity.ok ( "Json message sent to kafka topic" );
    }

}
