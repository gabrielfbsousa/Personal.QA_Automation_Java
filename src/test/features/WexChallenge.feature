Feature: Wex Challenge - Gabriel Ferreira

  @ID-0001 @TestSetup
  Scenario: Test Setup
    Given I open the browser at Google
    When I search for "Amazon Brasil"
    Then I select the result of "www.amazon.com.br"

  @ID-0002 @Check80Percent
  Scenario: Test Setup
    Given I open the browser at Amazon
    Then I search the product "iPhone"
    When I count the quantity of products found
    Then I count the quantity of "iPhone" found
    Then I check if "80"% or more than the result are actual "iPhone"

  @ID-0003 @CheckHigherPrice
  Scenario: Test Setup
    Given I open the browser at Amazon
    Then I search the product "iPhone"
    Then Check if the most expensive "iPhone" costs less than USD"2000"

  @ID-0004 @CheckCheaperPhone
  Scenario: Test Setup
    Given I open the browser at Amazon
    Then I search the product "iPhone"
    Then Check if the Cheapest "iPhone" is more expensive than the other products

