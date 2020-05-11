package com.esp30.smartMirror.controllers;

import com.esp30.smartMirror.data.Emotion;
import com.esp30.smartMirror.data.EmotionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DBController {

    @Autowired
    private EmotionRepository emotionRepository;
    
    private Logger logger = LoggerFactory.getLogger(DBController.class);

    @KafkaListener(topics = "p30-classification", groupId="db")
    public void listen(String msg) {
        logger.info("Received Message in group: " + msg);

        try {
            JSONObject json = new JSONObject(msg);
            Emotion emotion = new Emotion("Default User", (String) json.get("emote"));
            emotionRepository.save(emotion);
        }
        catch (JSONException err){
            logger.error("Error", err.toString());
        }
    }
}

