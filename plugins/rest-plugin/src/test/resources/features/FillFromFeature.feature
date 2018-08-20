#language:en
@fromfeature
Feature: Fill request from feature

  @fromfeature
  Scenario: Fill request from feature
    * user fill the request "first request from feature"
    * user add a header parameter with name "Content-Type" and value "application/json"
    * user add a header parameters
      | header-parameter-name-1 | header-parameter-value-1 |
      | header-parameter-name-2 | header-parameter-value-2 |
    * user add a query parameter with name "query-parameter" and value "query-parameter-value"
    * user add a query parameters
      | query-parameter-name-1 | query-parameter-value-1 |
      | query-parameter-name-2 | query-parameter-value-2 |
    * user add a body parameter with name "id" and value "11223344"
    * user add a body parameters
      | name  | Default_person            |
      | email | default_person@google.com |
    * user sends request
    * system returns "result"

    * user fill the request "second request from feature"
    * user add a query parameter with name "query-parameter-value-1" and get value from response on "first request from feature" from body by path "result" and apply mask "q1=(.*)\|"
    * user add a header parameter with name "h" and get value from response on "first request from feature" from body by path "result" and apply mask "h1=(.*)\|"
    * user add a body parameter with name "name" and get value from response on "first request from feature" from body by path "result" and apply mask "name=(.*)\|"
    * user add a body parameter with name "id" and get value from response on "first request from feature" from body by path "result" and apply mask "id=(.*)\|"
    * user add a query parameter with name "q2" and get value from response on "first request from feature" from header with name "header-parameter-name-1" and apply mask ""
    * user add a header parameter with name "Content-Type" and get value from response on "first request from feature" from header with name "Content-Type" and apply mask ""
    * user add a body parameter with name "email" and get value from response on "first request from feature" from header with name "header-parameter-name-2" and apply mask "-(.*)-"
    * user sends request
    * system returns "result"