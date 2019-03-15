#language: en
@data=$Data @test-non-critical
Feature: Test non critical

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

  @test-non-critical
  Scenario: Test non critical in fragments
    * user inserts fragment "fill fragment with non critical"
      | first name          | button name |
      | ${Admin.first name} | send        |
    * ? user checks in the element "first name" value "Alex"

  @non-critical-one-step
  Scenario: Failed non-critical test at the end of scenario
    * ? user is on the page "Non existent page"

  @non-critical-user-error
  Scenario: Check processing noncritical user error
    * test non critical user error