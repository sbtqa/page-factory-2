#language:en
@dаtatable
Feature: Datatable test

  @datatable-default
  Scenario: Header and param with params
    * user sends request for "api request with params test" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |
    * system returns "result with datatable" with parameters
      | query-parameter-name-1  | query-parameter-value-1  |
      | header-parameter-name-1 | header-parameter-value-1 |


  @datatable-placeholders
  Scenario: Header and param with placeholder params
    * user sends request for "api request with params test placeholders" with parameters
      | parameter-1 | parameter-value-1 |
      | parameter-2 | parameter-value-2 |
      | parameter-3 | Alex              |
      | header2     | header-value-2    |
    * system returns "result with datatable placeholders" with parameters
      | query-parameter-name-1  | new-parameter-value-1                                          |
      | header-parameter-name-1 | [{"value":"parameter-value-2", "visible":true, "name":"Alex"}] |
      | header2                 | header-value-2                                                 |