#language:en
@mutator
Feature: Mutator test

  Scenario: Mutator annotation test
    * user sends request for "api request with mutator test"
    * system returns "result with mutated values" with parameters
      | query-parameter-name-1  | LOWERCASEPARAM  |
      | header-parameter-name-1 | not null string |