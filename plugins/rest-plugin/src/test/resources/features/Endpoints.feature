#language:en
@endpoints
Feature: Api action test

  @get
  Scenario: get
    * user sends request for "get with json"
    * system returns "default client"

  @post
  Scenario: post
    * user sends request for "post with json"
    * system returns "result"

  @typed-arrays
  Scenario: Typed Arrays
    * user sends request for "typed arrays" with parameters
      | valuesString  | "should be quoted", \,one, two, three, four         |
      | valuesInt     | 1, 2, 3, 4, 5                                       |
      | valuesBoolean | true, false, true, true, true                       |
    * system returns "result"

  @put
  Scenario: put
    * user sends request for "put test"
    * system returns "result"

  @patch
  Scenario: patch
    * user sends request for "patch test"
    * system returns "result"

  @delete
  Scenario: delete
    * user sends request for "delete test" with parameters
      | client | query-parameter-value-1 |
    * system returns "result"
