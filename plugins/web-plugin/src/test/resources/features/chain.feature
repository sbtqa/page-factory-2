#language: en
Feature: Check fragments functional

  @chain
  Scenario: chain
    * user is on the page "Main"
    * user (click the button) "Contact"
    * user is on the page "Contact"

    * user (fill the field) "first name" "LEX"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
    * user (fill the field) "first name" "QWE"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
    * user (fill the field) "first name" "STOK"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"
    * user (fill the field) "first name" "QWE"
    * user (click the button) "send"
    * user (check that error message not contains) "Please specify your first name"

