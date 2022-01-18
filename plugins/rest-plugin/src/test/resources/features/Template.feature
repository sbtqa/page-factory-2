#language:en
@template
Feature: Test template placeholder replacing

  Scenario: Test template placeholder replacing
    * user sends request for "test replacer" with parameters
      | day15 | ""     |
      | day1  | null   |
      | day11 | "null" |
    * system returns "works correctly"

  @empty-objects
  Scenario: Test template placeholder replacing with empty objects
    * user sends request for "empty objects" with parameters
      | first | parameter     |
    * system returns "result"

  @empty-objects
  Scenario: Test template placeholder replacing with empty arrays
    * user sends request for "empty arrays" with parameters
      | first | parameter     |
    * system returns "result"

  @long-numbers
  Scenario: Test template placeholder replacing with long numbers
    * user sends request for "long numbers" with parameters
      | int       | 2147483647                                    |
      | intMin    | -2147483648                                   |
      | intNeg    | 2147483648                                    |
      | long      | 9223372036854775807                           |
      | longMin   | -9223372036854775808                          |
      | longNeg   | 9223372036854775808                           |
      | bigInt    | 99999999999999999999999999999999999           |
      | bigIntMin | -99999999999999999999999999999999999          |
      | float     | 9223372036854775807.85555565                  |
      | floatMin  | -9223372036854775807.85555565                 |
      | double    | 9223372036854775807.85555565                  |
      | doubleMin | -9223372036854775807.85555565                 |
      | bigDec    | 99999999999999999999999999999999999.85555565  |
      | bigDecMin | -99999999999999999999999999999999999.85555565 |
    * system returns "result"