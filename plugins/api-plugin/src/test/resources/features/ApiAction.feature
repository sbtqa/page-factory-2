#language:en
@apiaction
Feature: Api action test

  @get
  Scenario Outline: get
    * user activate <implementation> rest
    * user sends request for "<entry>"
    * system returns "default client"
    Examples:
      | implementation | entry         |
      | raw            | get with json |
      | entity         | get with json |

  @post
  Scenario Outline: post
    * user activate <implementation> rest
    * user sends request for "<entry>"
    * system returns "result"
    Examples:
      | implementation | entry         |
      | raw            | post with json |
      | entity         | post with json |

  @put
  Scenario Outline: put
    * user activate <implementation> rest
    * user sends request for "put test"
    * system returns "result"
    Examples:
      | implementation |
      | raw            |

  @patch @disabled
  Scenario Outline: patch
    * user activate <implementation> rest
    * user sends request for "patch test"
    * system returns "result"
    Examples:
      | implementation |
      | raw            |
      | entity         |

  @delete
  Scenario Outline: delete
    * user activate <implementation> rest
    * user sends request for "delete test"
    * system returns "result"
    Examples:
      | implementation |
      | raw            |
