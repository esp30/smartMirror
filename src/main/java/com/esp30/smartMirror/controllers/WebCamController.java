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

@Controller
public class WebCamController {
    private Logger logger = LoggerFactory.getLogger(WebCamController.class);

    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping("/mirror")
    public String exposeCamera(Model model) throws IOException{
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcam.open();
        byte[] imageBytes = WebcamUtils.getImageBytes(webcam, "png");
        CascadeClassifier faceDetector = new CascadeClassifier();

        File cascade = resourceLoader.getResource("classpath:static/haarcascade_frontalface_alt.xml").getFile();
        faceDetector.load(cascade.getAbsolutePath());

        Mat cvImage = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(cvImage, faceDetections);

        for(Rect rect : faceDetections.toArray()){
            Imgproc.rectangle(cvImage, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
        }

        MatOfByte mb = new MatOfByte();
        Imgcodecs.imencode(".png", cvImage, mb);
        byte[] cvImageBytes = mb.toArray();

        Base64.Encoder encoder = Base64.getEncoder();
        String image = "data:image/png;base64," + encoder.encodeToString(cvImageBytes);
        model.addAttribute("camFeedback", image);

        webcam.close();
        return "mirror";
    }

}
