@offline
Feature: F1 - Report Status

  Scenario Outline: Choose to Report

    Given a user with a medical condition
    When the user steps in front of the mirror
    And says "Status report"
    And the user is <emotion>
    Then the smart mirror should assess the user's mood as <emotionResult>

    Examples:
    | emotion | emotionResult | 
    |  happy  |      N/A      |   
    |   sad   |      N/A      |


    Scenario Outline: Report on Wake (RoW)

      Given a user with a medical condition
      And  said user just woke up
      When the user steps in front of the mirror
      And stands there for a few seconds
      And the user is <emotion>
      Then the smart mirror should assess the user's mood as <emotionResult>

    Examples:
    | emotion | emotionResult | 
    |  happy  |      N/A      |   
    |   sad   |      N/A      |