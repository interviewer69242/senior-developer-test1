Feature: Trade submission
  Scenario: A valid trade is submitted
    Given a valid trade request
    When the trade service submits it
    Then a trade identifier is returned
    And settlement receives the trade
