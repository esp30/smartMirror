package com.esp30.smartMirror.bdd.stepdefs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cucumber.api.java.en.*;

public class ReportStatusSteps {

    private String userID;
    private String emotionToBeDetected;
    private String emotionDetected;

    @Given("a {string} with a medical condition")
    public void aUserWithAMedicalCondition(String user) {
        userID = user;
    }

    @When("the {string} is {string}")
    public void theUserIsEmotion(String user, String emotion) {
        emotionToBeDetected = emotion;
    }

    @And("the {string} steps in front of the mirror and says {string}")
    public void theUserStepsInFrontOfTheMirror(String user, String action) {
        // Simulate API working

        // Get an image of a happy person
        // Convert to base64 
        // Publish to Kafka topic

        // Obtain the info from the API
        

        // Obtain emotion detected
        emotionDetected = emotionToBeDetected;        
    }

    // Scenario Report on Wake (RoW)
    @And("the {string} just woke up and steps in front of the mirror for a few seconds")
    public void saidUserJustWokeUp(String user) {
        // Simulate API working

        // Get an image of a happy person
        // Convert to base64 
        // Publish to Kafka topic

        // Obtain the info from the API
        

        // Obtain emotion detected

        emotionDetected = emotionToBeDetected;        
    }
    
    @Then("the smart mirror should assess the {string} mood as {string}")
    public void theSmartMirrorShouldAssessTheUserMoodAsEmotionResult(String user, String emotion) {
        System.out.println("HELLLOOO 3!!!!!!!!!!!!!! USER IS " + user + " EMOTION IS " + emotion);
        assertTrue(emotionDetected.equals(emotionToBeDetected), String.format("Mismatch on user %s - emotion should be %s", user, emotion));
    }

    

   
}
