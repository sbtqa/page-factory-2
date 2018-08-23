#language:en
@dаtatable
Feature: Datatable test

  Scenario: Header and param with params
    * user sends request for "api request with params test" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |
    * system returns "result" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |
