Feature: F2 - Remotely Check Patient Status

  Scenario: Notification

    Given a user with a medical condition

    And a medical professional that cares for said user

    And a mobile app that accesses the system's public API

    And internet connection

    When the system records a status (mood) change with the user

    Then the medical professional should be notified via the mobile app


  Scenario: Latest Reports	
  
  Given a user with a medical condition

  And a medical professional that cares for said user

  And a mobile app that accesses the system's public API

  And internet connection

  When the medical professional wishes to check on their patient latest reports

  Then the medical professional should be able to access that information using the mobile app
