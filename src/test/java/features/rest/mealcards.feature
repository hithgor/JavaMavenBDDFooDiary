# new feature
# Tags: optional

Feature: Mealcards

  @loginRequired
  Scenario Outline: User created mealcards are assigned appropriate names

   # Given Date created payload "\"dateCreated\":\"2020-03-19\""
    Given Working payload "<dateCreated>"
    When User calls "GetMealcardsEndpoint" with "Post" request
    Then Consecutive Mealcards are named approprietly

    Examples:
    | dateCreated |
    | \"dateCreated\":\"2020-03-19\" |
    | \"dateCreated\":\"2020-03-20\" |
    | \"dateCreated\":\"2020-03-21\" |

  @loginRequired
  Scenario: User saves mealcards and can retrieve them

    Given Valid mealcards payload
    When User calls "SaveMealcardsEndpoint" with "Post" request
    Then the API call got status code 302
    And Verification by GetMealcardsEndpoint is successful


