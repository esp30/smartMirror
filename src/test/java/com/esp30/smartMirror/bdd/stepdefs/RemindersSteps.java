package com.esp30.smartMirror.bdd.stepdefs;

import cucumber.api.java.en.*;

public class RemindersSteps {
    @Given("a user")
    public void aUser() {
        
    }

    @When("the user says {string}")
    public void theUserSays(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // throw new cucumber.api.PendingException();
    }

    @Then("the smart mirror should set a reminder with an associated alarm set to go off at {int} o'clock")
    public void theSmartMirrorShouldSetAReminderWithAnAssociatedAlarmSetToGoOffAtOClock(int arg0) {
    }
}
