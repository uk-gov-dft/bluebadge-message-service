@users-put
Feature: Verify message end points are secured

  Background:
    * url baseUrl

  Scenario: Denied when send a message without auth header
    Given path 'messages'
    And request {template: "RESET_PASSWORD", emailAddress:"a@b.com", attributes:{fullName:"bob", ***REMOVED***}}
    When method POST
    Then status 401
