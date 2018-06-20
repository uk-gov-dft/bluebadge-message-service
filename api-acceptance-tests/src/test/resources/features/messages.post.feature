@users-put
Feature: Verify messages create

  Background:
    * url baseUrl

  Scenario: Create password rest request
    Given path 'messages/send***REMOVED***-email'
    And request {userId: -1, name: "bob", emailAddress:"a@b.c", isNewUser: true}
    When method POST
    Then status 200
    And match $.data contains {uuid:"#notnull"}