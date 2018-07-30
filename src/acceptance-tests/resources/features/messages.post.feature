@users-put
Feature: Verify messages create

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Send a message
    Given path 'messages'
    And request {template: "RESET_PASSWORD", emailAddress:"a@b.com", attributes:{fullName:"bob", ***REMOVED***}}
    When method POST
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Bad request for unknown template
    Given path 'messages'
    And request {template: "TEST_TEMPLATE_1", emailAddress:"a@b.com", attributes:{name:"bob", age:2}}
    When method POST
    Then status 400
    And match $.error contains {message:"Unknown message template: TEST_TEMPLATE_1"}