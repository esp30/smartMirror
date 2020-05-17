@online
Feature: F2 - Remotely Check Patient Status

  Scenario: Emotion Trends

    Given a medical professional that needs to know the trends of emotions for all users
    And a mobile app that accesses the system's public API
    Then the medical professional should be able to obtain all emotions registered, without any user identification


  Scenario Outline: Latest Reports
  
    Given a <user> with a medical condition
    And a medical professional that cares for said user
    And a mobile app that accesses the system's public API
    When the medical professional wishes to check on their <user> latest reports
    Then the medical professional should be able to access the latest reports of the <user> <emotions> using the mobile app

    Examples:
    | user     | emotions |
    |    Maria |   N/A    |   
    |     José |   N/A    |   
    | Fernando |   N/A    | 