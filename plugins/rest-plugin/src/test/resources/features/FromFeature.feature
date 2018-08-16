#language:en
@fromfeature
Feature: Fill request from feature

  @fromfeature
  Scenario: Fill request from feature
    * user fill the request "request from feature"
    * user add a header with name "Content-Type" and value "application/json"
    * user add a headers
      | header-parameter-name-1 | header-parameter-value-1 |
      | header-parameter-name-2 | header-parameter-value-2 |
    * user add a query parameter with name "query-paramter" and value "query-parameter-value"
    * user add a query parameters
      | query-parameter-name-1 | query-parameter-value-1 |
      | query-parameter-name-2 | query-parameter-value-2 |
    * user add a body parameter with name "id" and value "11223344"
    * user add a body parameters
      | name  | Default_person            |
      | email | default_person@google.com |
    * user sends request
    * system returns "result"