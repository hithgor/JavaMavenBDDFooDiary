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
      | 4         | March       | 2020       | 3                 |
      | 5        | March       | 2020       | 2                 |

  @SeleniumSetUp
  Scenario Outline: User can add and remove mealcard at DOM level

    Given User is on "CaloriestrackerPage"
    And Full successful login procedure
    When User chooses date day <targetDay> month "<targetMonth>" year <targetYear>
    And User clicks on element XPATH "//button[@id='todayBtn']"
    Then <numberOfMealcards> Mealcards are displayed
    When User clicks on element XPATH "//button[@id='addMealButton']"
    Then <numberOfMealcards1> Mealcards are displayed
    When User clicks on element XPATH "//div[3]//div[1]//div[1]//button[1]"
    Then <numberOfMealcards> Mealcards are displayed

    Examples:
      | targetDay | targetMonth | targetYear | numberOfMealcards | numberOfMealcards1 |
      | 5         | March       | 2020       | 2                 | 3                  |


  @SeleniumSetUp
  Scenario Outline: User retrieves 3 mealcards removes middle one and adds another
      #Mealcard 3 should change title from Dinner to Lunch as it becomes a new mealcard 2

    Given User is on "CaloriestrackerPage"
    And Full successful login procedure
    When User chooses date day <targetDay> month "<targetMonth>" year <targetYear>
    And User clicks on element XPATH "//button[@id='todayBtn']"
    Then <numberOfMealcards1> Mealcards are displayed
    And Check for mealcard naming convention
    Then <numberOfMealcards1> Mealcards are displayed
    Examples:
      | targetDay | targetMonth | targetYear | numberOfMealcards1 |
      | 6         | March       | 2020       | 3                  |





