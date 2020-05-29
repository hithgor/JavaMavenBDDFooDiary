# new feature
# Tags: optional

Feature: UserLogin

  @SeleniumSetUp
  Scenario: User cannot pick a date without logging in

    Given User is on "CaloriestrackerPage"
    When User clicks on element XPATH "/html[1]/body[1]/button[1]"
    Then Login form is displayed