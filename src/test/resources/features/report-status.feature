Feature: Report Status

  Scenario: Choose to Report

    Given a user with a medical condition

    When the user steps in front of the mirror

    And says "Status report"

    Then the smart mirror should assess his status (mood)