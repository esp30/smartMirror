package com.esp30.smartMirror.controllers;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.detection.CLMFaceDetector;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class WebCamController {
    private Logger logger = LoggerFactory.getLogger(WebCamController.class);

    private CLMFaceDetector recogniser = new CLMFaceDetector();

    @GetMapping("/mirror")
    public String exposeCamera(Model model){
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcam.open();
        BufferedImage photo = webcam.getImage();
        FImage detPhoto = new FImage(bImageToPixelArray(photo));
        List<CLMDetectedFace> faces = recogniser.detectFaces(detPhoto);

        for(DetectedFace face : faces){
            Graphics2D detFaces = photo.createGraphics();
            Rectangle bound = face.getBounds();
            detFaces.setColor(Color.red);
            detFaces.drawRect((int)bound.x, (int)bound.y, (int)bound.width, (int)bound.height);
            detFaces.dispose();
        }

        ByteArrayOutputStream toConvert = new ByteArrayOutputStream();
        byte[] imageBytes;
        try {
            ImageIO.write(photo, "png", toConvert);
            toConvert.flush();
            imageBytes = toConvert.toByteArray();
            toConvert.close();

            Base64.Encoder encoder = Base64.getEncoder();
            String image = "data:image/png;base64," + encoder.encodeToString(imageBytes);

            model.addAttribute("camFeedback", image);
        }
        catch(IOException ex){}

        webcam.close();
        return "mirror";
    }

    // taken from https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
    private static float[][] bImageToPixelArray(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        float[][] result = new float[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = (float) argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = (float) argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }


}
