package com.esp30.smartMirror.cucumber;

/**
 * smartMirror - com.esp30.smartMirror.cucumber.ReportStatusSteps <br>
 *
 * @author Pedro Teixeira pedro.teix@ua.pt
 * @version 1.0 - April 30, 2020
 */
public class ReportStatusSteps {
    @cucumber.api.java.en.Given("^a user with a medical condition$")
    public void aUserWithAMedicalCondition() {
    }

    @cucumber.api.java.en.When("^the user steps in front of the mirror$")
    public void theUserStepsInFrontOfTheMirror() {
    }

    @cucumber.api.java.en.And("^says \"([^\"]*)\"$")
    public void says(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @cucumber.api.java.en.And("^the user is happy$")
    public void theUserIsHappy() {
    }

    @cucumber.api.java.en.Then("^the smart mirror should assess his status \\(mood\\) as happy$")
    public void theSmartMirrorShouldAssessHisStatusMoodAsHappy() {
    }

    @cucumber.api.java.en.And("^said user just woke up$")
    public void saidUserJustWokeUp() {
    }

    @cucumber.api.java.en.And("^stands there for a few seconds$")
    public void standsThereForAFewSeconds() {
    }
}
