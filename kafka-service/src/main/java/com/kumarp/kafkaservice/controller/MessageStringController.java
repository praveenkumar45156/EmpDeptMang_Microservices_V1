package com.kumarp.kafkaservice.controller;

import com.kumarp.kafkaservice.producer.KafkaStringProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/string/kafka")
public class MessageStringController {

    @Autowired
    private KafkaStringProducer producer;

    @GetMapping("/publish")
    public ResponseEntity<String> publish(@RequestParam("message") String message){
        producer.sendMessage ( message );
        return ResponseEntity.ok ( "Message sent to the topic" );
    }
}
