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
    * user add query parameter "query-parameter-value-1" from response on "first request from feature" body "result" mask "q1=(.*)\|"
    * user add header parameter "h" from response on "first request from feature" body "result" mask "h1=(.*)\|"
    * user add body parameter "name" from response on "first request from feature" body "result" mask "name=(.*)\|"
    * user add body parameter "id" from response on "first request from feature" body "result" mask "id=(.*)\|"
    * user add query parameter "q2" from response on "first request from feature" header "header-parameter-name-1"
    * user add header parameter "Content-Type" from response on "first request from feature" header "Content-Type"
    * user add body parameter "email" from response on "first request from feature" header "header-parameter-name-2" mask "-(.*)-"
    * user sends request
    * system returns "result"