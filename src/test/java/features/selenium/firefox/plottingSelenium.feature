# new feature
# Tags: optional

Feature: User stats plot

  @SeleniumSetUpFirefox
  Scenario: User logs in and checks his consumption plot
    Given User is on "CaloriestrackerPage"
    And Full successful login procedure
    And Element with xpath "//div[@class='svg-container']" does not exist
    When User clicks on element XPATH "//button[@id='toggleStatsBtn']"
    Then Element with xpath "//div[@class='svg-container']" is displayed


  @SeleniumSetUpFirefox
  Scenario: User cannot prompt plot functionality without logging in
    Given User is on "CaloriestrackerPage"
    Then Element with xpath "//div[@class='svg-container']" does not exist
    And Element with xpath "//button[@id='toggleStatsBtn']" does not exist


