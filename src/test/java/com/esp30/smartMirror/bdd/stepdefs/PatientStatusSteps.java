package com.esp30.smartMirror.bdd.stepdefs;
import com.esp30.smartMirror.data.Emotion;
import cucumber.api.java.en.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class PatientStatusSteps {

//    @Autowired
//    Environment environment;
//    String port = environment.getProperty("local.server.port");
    String port = "30043";

    CloseableHttpClient httpClient = HttpClients.createDefault();

    String patientName = "";
    int patientID = 0;
    String apiResponse;

    // Scenario 1 - Emotion Trends
    @Given("a medical professional that needs to know the trends of emotions for all users")
    public void aMedicalProfessionalThatNeedsToKnowTheTrendsOfEmotionsForAllUsers() {
        System.out.println("Doctor John wants to check on his patients.");
    }

    @And("a mobile app that accesses the system's public API")
    public void aMobileAppThatAccessesTheSystemSPublicAPI() {
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(Integer.parseInt(port))
                    .setPath("/emotions")
                    .build();
            HttpGet request = new HttpGet(uri);
//            HttpGet request = new HttpGet("http://localhost:"+port+"/emotions");
            try {
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                apiResponse = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                System.err.println("Error accessing API - emotions retrieval failed.");
            }
        } catch (URISyntaxException e) {
            System.err.println("Error building URI.");
        }
//        try {
//            emotions = controller.emotions();
//            System.out.println("===== EMOTIONS =====");
//            for (Emotion emo : emotions) {
//                System.out.println(emo.getUser() + ": " + emo.getValue());
//            }
//            System.out.println("====================");
//        } catch (Exception e) {
//            valid = false;
//            System.err.println("Error accessing API - emotions retrieval failed.");
//        }
    }

    @Then("the medical professional should be able to obtain all emotions registered without any user identification")
    public void theMedicalProfessionalShouldBeAbleToObtainAllEmotionsRegisteredWithoutAnyUserIdentification() {
        System.out.println("/emotions: "+apiResponse);
    }

    // Scenario 2 - Latest Reports
    @Given("a medical professional that takes care of {string}")
    public void aMedicalProfessionalThatCaresForSaidUser (String user, int id) {
        patientName = user;
        patientID = id;
    };

    @When("the medical professional wishes to check on their {string} latest reports")
    public void theMedicalProfessionalWishesToCheckOnTheirUserLatestReports() {
        System.out.println("Doctor John wants to check on " + patientName);
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(Integer.parseInt(port))
                    .setPath("/useremotions")
                    .setParameter("id",Integer.toString(patientID))
                    .build();
            HttpGet request = new HttpGet(uri);
//            HttpGet request = new HttpGet("http://localhost:"+port+"/useremotions");
            try {
                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                apiResponse = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                System.err.println("Error accessing API - emotions retrieval failed.");
            }
        } catch (URISyntaxException e) {
            System.err.println("Error building URI.");
        }
//        try {
//            emotions = controller.userEmotions(Integer.toString(patientID));
//        } catch (Exception e) {
//            valid = false;
//            System.err.println("Error accessing API - emotions retrieval failed.");
//        }
    }

    @Then("the medical professional should be able to access the latest reports of the {string} {string} using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfTheUserEmotionsUsingTheMobileApp() {
        System.out.println("/useremotions{"+patientID+"}: "+apiResponse);
    }
}
