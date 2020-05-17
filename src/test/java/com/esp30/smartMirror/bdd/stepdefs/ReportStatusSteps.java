package com.esp30.smartMirror.bdd.stepdefs;

import cucumber.api.java.en.*;

public class ReportStatusSteps {
    @Given("^a user with a medical condition$")
    public void aUserWithAMedicalCondition() {
    }

    @When("^the user steps in front of the mirror$")
    public void theUserStepsInFrontOfTheMirror() {
    }

    @And("^says \"([^\"]*)\"$")
    public void says(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @And("^the user is <emotion>$")
    public void theUserIsEmotion() {
    }

    @Then("^the smart mirror should assess the user's mood as <emotionResult>$")
    public void theSmartMirrorShouldAssessTheUserSMoodAsEmotionResult() {
    }

    @And("^said user just woke up$")
    public void saidUserJustWokeUp() {
    }

    @And("^stands there for a few seconds$")
    public void standsThereForAFewSeconds() {
    }
}
