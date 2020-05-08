@online
@not-implemented
Feature: F3 - Reminders

  Scenario: Set Reminder

    Given a user

    And internet connection

    When the user says "Set reminder for 10 o'clock"

    Then the smart mirror should set a reminder with an associated alarm set to go off at 10 o'clock