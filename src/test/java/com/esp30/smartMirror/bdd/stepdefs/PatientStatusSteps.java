package com.esp30.smartMirror.bdd.stepdefs;
import com.esp30.smartMirror.data.Emotion;
import cucumber.api.java.en.*;
import com.esp30.smartMirror.controllers.ApiController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class PatientStatusSteps {

    @Autowired ApiController controller;

    boolean valid = true;
    String patientName;

    // Scenario 1 - Emotion Trends
    @Given("a medical professional that needs to know the trends of emotions for all users")
    public void aMedicalProfessionalThatNeedsToKnowTheTrendsOfEmotionsForAllUsers() throws Exception {
        System.out.println("Doctor John wants to check on his patients.");
    }

    @And("a mobile app that accesses the system's public API")
    public void aMobileAppThatAccessesTheSystemSPublicAPI() {
        try {
            ArrayList<Emotion> emotions = controller.emotions();
            System.out.println("===== EMOTIONS =====");
            for (Emotion emo : emotions) {
                System.out.println(emo.getUser() + ": " + emo.getValue());
            }
            System.out.println("====================");
        } catch (Exception e) {
            valid = false;
            System.err.println("Error accessing API - emotions retrieval failed.");
        }
    }

    @Then("the medical professional should be able to obtain all emotions registered without any user identification")
    public void theMedicalProfessionalShouldBeAbleToObtainAllEmotionsRegisteredWithoutAnyUserIdentification() {
        assert(valid);
    }

    // Scenario 2 - Latest Reports
    @Given("a medical professional that takes care of {string}")
    public void aMedicalProfessionalThatCaresForSaidUser (String user) {
        patientName = user;
    };

    @When("the medical professional wishes to check on their {string} latest reports")
    public void theMedicalProfessionalWishesToCheckOnTheirUserLatestReports() {
        System.out.println("Doctor John wants to check on " + patientName);
        // Impossible to test rn since the method userEmotions takes the patient ID as input parameter
        // In this situation, it's impossible to know the patient id
    }

    @Then("the medical professional should be able to access the latest reports of the {string} {string} using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfTheUserEmotionsUsingTheMobileApp() {
    }
}
