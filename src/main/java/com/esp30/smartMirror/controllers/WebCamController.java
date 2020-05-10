package com.esp30.smartMirror.controllers;

import com.esp30.smartMirror.data.Photo;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.*;

@Controller
public class WebCamController {
    private Logger logger = LoggerFactory.getLogger(WebCamController.class);
    
    private static int UNIQUE_IMAGE_ID = 0;
    private final Properties prod_properties = new Properties();
    private final Properties con_properties = new Properties();
    private KafkaProducer kafkaProducer;
    private Consumer<Long, String> kafkaConsumer;
    
    private HashMap<String, String> emotionsLUT;
    
    @PostConstruct
    public void init(){
        logger.info("Initializing webcam controller");
        prod_properties.put("bootstrap.servers", "192.168.160.103:9093");
        prod_properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prod_properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        con_properties.put("bootstrap.servers", "192.168.160.103:9093");
        con_properties.put("key.deserializer", LongDeserializer.class.getName());
        con_properties.put("value.deserializer" , StringDeserializer.class.getName());
        con_properties.put("group.id" , "p30");

        emotionsLUT = new HashMap<>();
        emotionsLUT.put("Surprise", "\ud83d\ude2e");
        emotionsLUT.put("Angry", "\ud83d\ude20");
        emotionsLUT.put("Sad", "\ud83d\ude22");
        emotionsLUT.put("Disgust", "\ud83e\udd2e");
        emotionsLUT.put("Fear", "\ud83d\ude31");
        emotionsLUT.put("Happy", "\uD83D\uDE0A");
        emotionsLUT.put("Neutral", "\ud83d\ude10");
    }
    
    @Autowired
    ResourceLoader resourceLoader;

    String lastPhoto;
    String lastSmileDetections = "";


    @ModelAttribute(value = "photoTaken")
    public Photo newPhoto(){
        return new Photo();
    }

    @GetMapping("/mirror")
    public ModelAndView exposeCamera(Model model){
        if(lastPhoto != null){
            model.addAttribute("camFeedback", lastPhoto);
        }
        if(lastSmileDetections != null){
            logger.info("detected smile");
            if(!lastSmileDetections.equals("")) {
//                model.addAttribute("emotion", "\uD83D\uDE0A");
                  model.addAttribute("emotion", emotionsLUT.get(lastSmileDetections));
            }
            else model.addAttribute("emotion", "");
        }

        return new ModelAndView("mirror", "command", model);
    }

    @RequestMapping(value = "/index",method = RequestMethod.POST)
    public View processPhotoIndex(Model model, @ModelAttribute("photoTaken") Photo photoTaken) throws UnsupportedEncodingException, IOException{
        if(photoTaken.getB64Encoded() != null){
            Base64.Decoder decoder = Base64.getDecoder();
            String photo = new String(photoTaken.getB64Encoded().getBytes(), "UTF-8");
            byte[] imageBytes = decoder.decode(photo.getBytes("UTF-8"));

            Mat cvImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

            MatOfByte mb = new MatOfByte();
            Imgcodecs.imencode(".png", cvImage, mb);
            byte[] cvImageBytes = mb.toArray();

            Base64.Encoder encoder = Base64.getEncoder();
            String image = "data:image/png;base64," + encoder.encodeToString(cvImageBytes);
            lastPhoto = image;
            lastSmileDetections = null;

            kafkaProducer = new KafkaProducer(prod_properties);

            String jsonObject = "{\"id\": \"" + UNIQUE_IMAGE_ID + "\", \"image\": \"" + image + "\"}";
            int currentId = UNIQUE_IMAGE_ID;
            UNIQUE_IMAGE_ID++;
            logger.info(jsonObject);
            try{
                logger.info("Producing kafka string message!");
                kafkaProducer.send(new ProducerRecord("p30-photos", Integer.toString(0), jsonObject));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                kafkaProducer.close();
            }

            // Changed so sync waiting for message cause async wasn't working as expected
            kafkaConsumer = new KafkaConsumer(con_properties);
            kafkaConsumer.subscribe(Collections.singletonList("p30-classification"));

            // 3 second wait total
            final int giveUp = 3;   int noRecordsCount = 0;

            while (true) {
                final ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(1000);

                if (consumerRecords.count() == 0) {
                    noRecordsCount++;
                    if (noRecordsCount > giveUp) break;
                    else continue;
                }

                consumerRecords.forEach(record -> {
                    int id = -1;
                    String emote = "";
                    try {
                        JSONObject json = new JSONObject(record.value());
                        id = Integer.parseInt((String)json.get("id"));
                        emote = (String)json.get("emote");
                    } catch (JSONException ex) {
                    }
                    if(id == currentId){
                        lastSmileDetections = emote;
                        logger.info("Emote received: " + emote);
                    }
                });
                kafkaConsumer.commitAsync();
            }
            kafkaConsumer.close();
            if(lastSmileDetections == null) logger.warn("Couldn't get any emote!");
        }
        return new RedirectView("/index");
    }

    @RequestMapping(value = "/mirror",method = RequestMethod.POST)
    public View processPhotoMirror(Model model, @ModelAttribute("photoTaken") Photo photoTaken) throws UnsupportedEncodingException, IOException{
        if(photoTaken.getB64Encoded() != null){
            Base64.Decoder decoder = Base64.getDecoder();
            String photo = new String(photoTaken.getB64Encoded().getBytes(), "UTF-8");
            byte[] imageBytes = decoder.decode(photo.getBytes("UTF-8"));

//
//            CascadeClassifier faceDetector = new CascadeClassifier();
//            CascadeClassifier smileDetector = new CascadeClassifier();
//            String tempDir = "frontal";
//            Path tmp = Files.createTempFile(tempDir, "cv");
//            Files.copy(resourceLoader.getResource("classpath:static/haarcascade_frontalface_alt.xml").getInputStream(), tmp, StandardCopyOption.REPLACE_EXISTING);
//            faceDetector.load(tmp.toString());
//
//            tempDir = "smile";
//            tmp = Files.createTempFile(tempDir, "cv");
//            Files.copy(resourceLoader.getResource("classpath:static/haarcascade_smile.xml").getInputStream(), tmp, StandardCopyOption.REPLACE_EXISTING);
//            smileDetector.load(tmp.toString());

            Mat cvImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

//            MatOfRect faceDetections = new MatOfRect();
//            MatOfRect smileDetections = new MatOfRect();
//            faceDetector.detectMultiScale(cvImage, faceDetections);
//            smileDetector.detectMultiScale(cvImage, smileDetections, 1.8, 20, 0, new Size(0,0), new Size(1000, 1000));

//            for(Rect rect : faceDetections.toArray()){
//                Imgproc.rectangle(cvImage, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
//            }

            MatOfByte mb = new MatOfByte();
            Imgcodecs.imencode(".png", cvImage, mb);
            byte[] cvImageBytes = mb.toArray();

            Base64.Encoder encoder = Base64.getEncoder();
            String image = "data:image/png;base64," + encoder.encodeToString(cvImageBytes);
            lastPhoto = image;
//            lastSmileDetections = smileDetections;
            lastSmileDetections = null;
         
            kafkaProducer = new KafkaProducer(prod_properties);
         
            String jsonObject = "{\"id\": \"" + UNIQUE_IMAGE_ID + "\", \"image\": \"" + image + "\"}";
            int currentId = UNIQUE_IMAGE_ID;
            UNIQUE_IMAGE_ID++;
            logger.info(jsonObject);
            try{
                logger.info("Producing kafka string message!");
                kafkaProducer.send(new ProducerRecord("p30-photos", Integer.toString(0), jsonObject));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                kafkaProducer.close();
            }
            
            
            // Changed so sync waiting for message cause async wasn't working as expected
            kafkaConsumer = new KafkaConsumer(con_properties);
            kafkaConsumer.subscribe(Collections.singletonList("p30-classification"));
            
            
            // 3 second wait total
            final int giveUp = 3;   int noRecordsCount = 0;

            while (true) {
                final ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(1000);

                if (consumerRecords.count() == 0) {
                    noRecordsCount++;
                    if (noRecordsCount > giveUp) break;
                    else continue;
                }
                
                consumerRecords.forEach(record -> {
//                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
//                            record.key(), record.value(),
//                            record.partition(), record.offset());
                    int id = -1;
                    String emote = "";
                    try {
                        JSONObject json = new JSONObject(record.value());
                        id = Integer.parseInt((String)json.get("id"));
                        emote = (String)json.get("emote");
                    } catch (JSONException ex) {
                    }
                    if(id == currentId){
                        lastSmileDetections = emote;
                        logger.info("Emote received: " + emote);
                    }
                });

                kafkaConsumer.commitAsync();
            }
            kafkaConsumer.close();
            
            if(lastSmileDetections == null) logger.warn("Couldn't get any emote!");
            
        }
        return new RedirectView("/mirror");

    }
//    
//    @KafkaListener(topics = "p30-test-topic2", groupId="p30")
//    public String listen(String msg)
//    {
//        logger.info("Received Messasge in group foo: " + msg);
//       
//        // reload the page with the emoticon ready
//    }
//    


    
    // Overriding default boostrap server for the kafka listener callback stuff
    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
        Map<String, Object> props = properties.buildConsumerProperties();
        props.put(org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "192.168.160.103:9093");
        return new DefaultKafkaConsumerFactory<>(props);
    }


}
