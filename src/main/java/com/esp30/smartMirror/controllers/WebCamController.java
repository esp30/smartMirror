package com.esp30.smartMirror.controllers;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.image.BufferedImage;
import java.util.Base64;

@Controller
public class WebCamController {
    Logger logger = LoggerFactory.getLogger(WebCamController.class);

    @GetMapping("/mirror")
    public String exposeCamera(Model model){
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        
        webcam.open();

        byte[] imageBytes = WebcamUtils.getImageBytes(webcam, "png");
        Base64.Encoder encoder = Base64.getEncoder();
        String image = "data:image/png;base64," + encoder.encodeToString(imageBytes);

        model.addAttribute("camFeedback", image);

        webcam.close();
        return "mirror";
    }


}
