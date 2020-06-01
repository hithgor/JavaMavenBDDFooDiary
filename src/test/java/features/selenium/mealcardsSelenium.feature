# new feature
# Tags: optional

Feature: Manipulating mealcards at DOM level

  @SeleniumSetUp
  Scenario Outline: User creates two mealcards and saves them
    Given User is on "CaloriestrackerPage"
    And Full successful login procedure
    When User chooses date day <targetDay> month "<targetMonth>" year <targetYear>
    And User clicks on element XPATH "//button[@id='todayBtn']"
    Then <numberOfMealcards> Mealcards are displayed

    Examples:
      | targetDay | targetMonth | targetYear | numberOfMealcards |
      | 3         | March       | 2020       | 4                 |
  #  |4 | March | 2020 | 3                                     |
  #  |18 | March | 2020 | 2                                    |



