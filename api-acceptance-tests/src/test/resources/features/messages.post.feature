@users-put
Feature: Verify messages create

  Background:
    * url baseUrl

  Scenario: Create password rest request
    Given path 'messages/send-email'
    And request {userId: -1}
    When method POST
    Then status 200
