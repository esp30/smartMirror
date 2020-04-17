/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esp30.smartMirror.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DBController {
    
    private static final Logger logger = LoggerFactory.getLogger(DBController.class);

    
    @KafkaListener(topics = "happy-people", groupId="p30")
    public void listen(String msg)
    {
        logger.info("Received Messasge in group foo: " + msg.substring(0, 20));
        // Write info to DB
    }
    
}

