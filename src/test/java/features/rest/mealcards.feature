# new feature
# Tags: optional

Feature: Mealcards

  @loginRequired
  Scenario Outline: User created mealcards are assigned appropriate names

   # Given Date created payload "\"dateCreated\":\"2020-03-19\""
    Given Date created payload "<dateCreated>"
    When User calls "GetMealcardsEndpoint" with "Post" request
    Then Consecutive Mealcards are named approprietly

    Examples:
    | dateCreated |
    | \"dateCreated\":\"2020-03-19\" |
    | \"dateCreated\":\"2020-03-20\" |
    | \"dateCreated\":\"2020-03-21\" |





