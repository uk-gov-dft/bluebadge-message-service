@create-messages
Feature: Verify messages create

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Send a message
    Given path 'messages'
    And request {template: "RESET_PASSWORD", emailAddress:"a@b.com", attributes:{fullName:"bob", ***REMOVED***}}
    When method POST
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Bad request for cannot deserialize invalid enum
    Given path 'messages'
    And request {template: "TEST_TEMPLATE_1", emailAddress:"a@b.com", attributes:{name:"bob", age:2}}
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"template","reason":"`TEST_TEMPLATE_1` is not one of the expected values; [NEW_USER, RESET_PASSWORD, PASSWORD_RESET_SUCCESS, APPLICATION_SUBMITTED, SAVE_AND_RETURN].","message":"InvalidFormat.template","location":null,"locationType":null}

  Scenario: Bad request for bean validation
    Given path 'messages'
    And request {template: "NEW_USER", emailAddress: null, attributes:{name:"bob", age:2}}
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"emailAddress","reason":"must not be null","message":"NotNull.messageDetails.emailAddress","location":null,"locationType":null}