@version
Feature: Verify incorrect version fails

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = 'application/vnd.bluebadge-api.v0+json, application/json'

  Scenario: Verify bean validation gives common response wrapped error
    Given path 'messages'
    And request {template: "RESET_PASSWORD", emailAddress:"a@b.com", attributes:{fullName:"bob", ***REMOVED***}}
    When method POST
    Then status 406