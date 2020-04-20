package com.esp30.smartMirror.controllers;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
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
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.util.Base64;
import java.util.Properties;
import javax.annotation.PostConstruct;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;

@Controller
public class WebCamController {
    private Logger logger = LoggerFactory.getLogger(WebCamController.class);
    
    //private final Properties properties = new Properties();
    //private KafkaProducer kafkaProducer;
    
/*    @PostConstruct
    public void init(){
        logger.info("Initializing webcam controller");
        properties.put("bootstrap.servers", "192.168.160.103:9021");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }*/
    
    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/mirror")
    public String exposeCamera(Model model) throws IOException{
        
        //kafkaProducer = new KafkaProducer(properties);
      
        
        //Webcam webcam = Webcam.getDefault();
        //webcam.setViewSize(WebcamResolution.VGA.getSize());

        //webcam.open();
        //byte[] imageBytes = WebcamUtils.getImageBytes(webcam, "png");
        CascadeClassifier faceDetector = new CascadeClassifier();
        CascadeClassifier smileDetector = new CascadeClassifier();

        //File faceCascade = resourceLoader.getResource("classpath:static/haarcascade_frontalface_alt.xml").getFile();
        //faceDetector.load(faceCascade.getAbsolutePath());

        //File smileCascade = resourceLoader.getResource("classpath:static/haarcascade_smile.xml").getFile();
        //smileDetector.load(smileCascade.getAbsolutePath());

        //Mat cvImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

        MatOfRect faceDetections = new MatOfRect();
        MatOfRect smileDetections = new MatOfRect();
        //faceDetector.detectMultiScale(cvImage, faceDetections);
        //smileDetector.detectMultiScale(cvImage, smileDetections, 1.8, 20, 0, new Size(0,0), new Size(1000, 1000));

        //for(Rect rect : faceDetections.toArray()){
            //Imgproc.rectangle(cvImage, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
        //}
  
        //MatOfByte mb = new MatOfByte();
        //Imgcodecs.imencode(".png", cvImage, mb);
        //byte[] cvImageBytes = mb.toArray();

        Base64.Encoder encoder = Base64.getEncoder();
        //String image = "data:image/png;base64," + encoder.encodeToString(cvImageBytes);
        //model.addAttribute("camFeedback", image);
        
        //webcam.close();
        
        // Producing a kafka simple string msg. Should consist on the encoded img
        // string above and if it's the case, a note stating that the user is smilling
        
        if(!smileDetections.empty()){
        
/*            try{
                logger.info("Producing kafka string message!");
                kafkaProducer.send(new ProducerRecord("happy-people", Integer.toString(0), image));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                kafkaProducer.close();
            }*/
            
            model.addAttribute("emotion", "\uD83D\uDE0A");
        }
        else model.addAttribute("emotion", "");
        
   
        
        return "mirror";
    }

}
