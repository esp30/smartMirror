package com.esp30.smartMirror.bdd.stepdefs;
import cucumber.api.java.en.*;

public class PatientStatusSteps {

    // Scenario 1 - Emotion Trends
    @Given("a medical professional that needs to know the trends of emotions for all users")
    public void aMedicalProfessionalThatNeedsToKnowTheTrendsOfEmotionsForAllUsers() {
    }

    @And("a mobile app that accesses the system's public API")
    public void aMobileAppThatAccessesTheSystemSPublicAPI() {
    }

    @Then("the medical professional should be able to obtain all emotions registered without any user identification")
    public void theMedicalProfessionalShouldBeAbleToObtainAllEmotionsRegisteredWithoutAnyUserIdentification() {
    }

    // Scenario 2 - Latest Reports
    @Given("a medical professional that takes care of {string}")
    public void aMedicalProfessionalThatCaresForSaidUser (String user) {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("BYEEEEEEEEE1 " + user);
    };

    @When("the medical professional wishes to check on their {string} latest reports")
    public void theMedicalProfessionalWishesToCheckOnTheirUserLatestReports(String user) {
        System.out.println("BYEEEEEEEEE2 " + user);
    }

    @Then("the medical professional should be able to access the latest reports of the {string} {string} using the mobile app")
    public void theMedicalProfessionalShouldBeAbleToAccessTheLatestReportsOfTheUserEmotionsUsingTheMobileApp(String user, String emotions) {
        System.out.println("BYEEEEEEEEE3 " + user);
        System.out.println(user + emotions);
    }
}
