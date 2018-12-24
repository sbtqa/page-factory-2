#language: en
Feature: background fragment

  @fragment
  Scenario: go to contact page
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"