package com.esp30.smartMirror.cucumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * smartMirror - com.esp30.smartMirror.cucumber.RemindersSteps <br>
 *
 * @author Pedro Teixeira pedro.teix@ua.pt
 * @version 1.0 - April 30, 2020
 */
public class RemindersSteps {
    @cucumber.api.java.en.Given("^a user$")
    public void aUser() {
        //assertTrue(false); // TESTING PURPOSES -> SHOULD FAIL
    }

    @cucumber.api.java.en.When("^the user says \"([^\"]*)\"$")
    public void theUserSays(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // throw new cucumber.api.PendingException();
    }

    @cucumber.api.java.en.Then("^the smart mirror should set a reminder with an associated alarm set to go off at (\\d+) o'clock$")
    public void theSmartMirrorShouldSetAReminderWithAnAssociatedAlarmSetToGoOffAtOClock(int arg0) {
    }
}
