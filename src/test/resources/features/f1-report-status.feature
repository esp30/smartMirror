@offline
Feature: F1 - Report Status

  Scenario Outline: Choose to Report

    Given a <user> with a medical condition
    When the <user> is <emotion>
    And  the <user> steps in front of the mirror and says "Status report"
    Then the smart mirror should assess the <user> mood as <emotionResult>

    Examples:
    | user         | emotion   | emotionResult   | 
    | "Mariana"    |  "happy"  |      "N/A"      |   
    | "Tiago"      |   "sad"   |      "N/A"      |
    | "Raquel"     |  "happy"  |      "N/A"      |


    Scenario Outline: Report on Wake (RoW)

      Given a <user> with a medical condition
      When the <user> is <emotion>
      And the <user> just woke up and steps in front of the mirror for a few seconds
      Then the smart mirror should assess the <user> mood as <emotionResult>

    Examples:
    | user         | emotion   | emotionResult   | 
    | "Mariana"    |  "happy"  |      "N/A"      |   
    | "Tiago"      |   "sad"   |      "N/A"      |
    | "Raquel"     |  "happy"  |      "N/A"      |