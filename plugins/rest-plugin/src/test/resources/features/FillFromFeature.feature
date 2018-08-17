#language:en
@fromfeature
Feature: Fill request from feature

  @fromfeature
  Scenario: Fill request from feature
    * user fill the request "request from feature"
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

    * user add a query parameter with name "query-parameter" and get value from response on "request from feature" in body by path "result" and apply mask "@(\w*)\."
    * user add a header parameter with name "query-parameter" and get value from response on "request from feature" in body by path "result"
    * user add a body parameter with name "query-parameter" and get value from response on "request from feature" in body by path "result"

    * user add a query parameter with name "query-parameter" and get value from response on "request from feature" in header with name "result" and apply mask "@(\w*)\."
    * user add a header parameter with name "query-parameter" and get value from response on "request from feature" in header with name "result" and apply mask "@(\w*)\."
    * user add a body parameter with name "query-parameter" and get value from response on "request from feature" in header with name "result" and apply mask "@(\w*)\."

#    * user sends request
#    * system returns "result"