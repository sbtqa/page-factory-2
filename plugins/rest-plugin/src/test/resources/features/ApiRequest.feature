#language:en
@apirequest
Feature: Api request test

  Scenario: Header and param
    * user sends request for "api request test"
    * system returns "result"

  Scenario: Header and param with params
    * user sends request for "api request with params test" with parameters
      | header-name      | header-value-1    |
      | parameter-name-1 | parameter-value-1 |
    * system returns "result" with parameters
      | header-name      | header-value-1    |
      | parameter-name-1 | parameter-value-1 |
