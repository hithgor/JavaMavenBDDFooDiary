# new feature
# Tags: optional

Feature: UserLogin

  @SeleniumSetUp
  Scenario: User cannot pick a date without logging in

    Given User is on "CaloriestrackerPage"
    When User clicks on element XPATH "/html[1]/body[1]/button[1]"
    Then Login form is displayed

  @SeleniumSetUp
  Scenario: User logs in with valid credentials

    Given User is on "CaloriestrackerPage"
    When User clicks on element XPATH "/html[1]/body[1]/button[1]"
    And User logs in with valid credentials
    Then Element with xpath "//div[contains(@class,'alert-success')]" is displayed

  @SeleniumSetUp
  Scenario: User logs in and logs out successfully

    Given User is on "CaloriestrackerPage"
    And Full successful login procedure
    When User clicks on element XPATH "//nav[@class='navbar']//form" by submit
    Then User waits up to 5 seconds for element XPATH "//div[@id='message' and contains(string(), 'You are now logged out')]"
    Then Element with xpath "//div[@id='message' and contains(string(), 'You are now logged out')]" is displayed