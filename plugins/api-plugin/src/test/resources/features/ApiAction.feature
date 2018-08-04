#language:en
@apiaction
Feature: Api action test

  @get
  Scenario: get
    * user sends request for "get with json"
    * system returns "default client"

  @post
  Scenario: post
    * user sends request for "post with json"
    * system returns "result"

  @put
  Scenario: put
    * user sends request for "put test"
    * system returns "result"

  @patch @disabled
  Scenario: patch
    * user sends request for "patch test"
    * system returns "result"

  @delete
  Scenario: delete
    * user sends request for "delete test"
    * system returns "result"
