@users-put
Feature: Verify messages create

  Background:
    * url baseUrl

  Scenario: Send a message
    Given path 'messages'
    And request {template: "TEST_TEMPLATE_1", emailAddress:"a@b.c", messageAttributes:{name:"bob", age:2}}
    When method POST
    Then status 200
    And match $.data contains {uuid:"#notnull"}