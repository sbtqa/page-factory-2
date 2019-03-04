#language: en
@data=$Data @test-non-critical
Feature: Data sources

  Background:
    * ? user is on the page "Main"
    * ? user clicks the button "Contact"
    * user is on the page "Contact"

  @failed-non-critical
  Scenario: Failed and skipped
    * ? failed step
    * ? (failed action)
    * user inserts fragment "fill fragment"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * user checks in the element "first name" value "Alex"
#    * ? user checkssdds in the element "first name" value "Alex"

  @test-non-critical
  Scenario: Data From Feature Tag 2
    * user inserts fragment "fill fragment with non critical"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * ? user checks in the element "first name" value "Alex"