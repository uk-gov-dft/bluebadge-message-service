@payments
Feature: Verify creating and updating gov pay secret

  Background:
    * url baseUrl
    * def SecretUtils = Java.type('uk.gov.service.bluebadge.test.utils.SecretUtils')
    * def secretUtils = new SecretUtils()

  Scenario: Update or create secret successfully with all values also dft user has access
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request { ***REMOVED*** templates: {"APPLICATION_SUBMITTED":"application submitted key", "NEW_USER":"new user secret", "RESET_PASSWORD":"reset  ***REMOVED***}}
    When method POST
    Then status 200
    And assert secretUtils.secretExistsForLa('SHROP')
    And assert secretUtils.getApiKeyForLa('SHROP') == 'theinitialsecret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'APPLICATION_SUBMITTED') == 'application submitted key'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'NEW_USER') == 'new user secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'RESET_PASSWORD') == 'reset password secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'PASSWORD_RESET_SUCCESS') == 'password reset secret'


  Scenario: Update secret successfully with no values
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {}
    When method POST
    Then status 200
    And assert secretUtils.secretExistsForLa('SHROP')
    And assert secretUtils.getApiKeyForLa('SHROP') == 'theinitialsecret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'APPLICATION_SUBMITTED') == 'application submitted key'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'NEW_USER') == 'new user secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'RESET_PASSWORD') == 'reset password secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'PASSWORD_RESET_SUCCESS') == 'password reset secret'

  Scenario: Update secret successfully with apiKey only
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request { ***REMOVED***
    When method POST
    Then status 200
    And assert secretUtils.secretExistsForLa('SHROP')
    And assert secretUtils.getApiKeyForLa('SHROP') == 'anewsecret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'APPLICATION_SUBMITTED') == 'application submitted key'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'NEW_USER') == 'new user secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'RESET_PASSWORD') == 'reset password secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'PASSWORD_RESET_SUCCESS') == 'password reset secret'

  Scenario: Update secret successfully with single template only
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {templates: {"APPLICATION_SUBMITTED":"application submitted new key"}}
    When method POST
    Then status 200
    And assert secretUtils.secretExistsForLa('SHROP')
    And assert secretUtils.getApiKeyForLa('SHROP') == 'anewsecret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'APPLICATION_SUBMITTED') == 'application submitted new key'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'NEW_USER') == 'new user secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'RESET_PASSWORD') == 'reset password secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'PASSWORD_RESET_SUCCESS') == 'password reset secret'

  Scenario: Update secret successfully la admin
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {templates: {"APPLICATION_SUBMITTED":"la admin"}}
    When method POST
    Then status 200
    And assert secretUtils.secretExistsForLa('SHROP')
    And assert secretUtils.getApiKeyForLa('SHROP') == 'anewsecret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'APPLICATION_SUBMITTED') == 'la admin'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'NEW_USER') == 'new user secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'RESET_PASSWORD') == 'reset password secret'
    And assert secretUtils.getTemplateKeyForLa('SHROP', 'PASSWORD_RESET_SUCCESS') == 'password reset secret'

  Scenario: Incorrect privs citizen app
    * def result = callonce read('./oauth2-citizen-app.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {templates: {"APPLICATION_SUBMITTED":"la admin"}}
    When method POST
    Then status 403

  Scenario: Incorrect privs la editor
    * def result = callonce read('./oauth2-shrop-la-editor.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {templates: {"APPLICATION_SUBMITTED":"la admin"}}
    When method POST
    Then status 403

  Scenario: Incorrect privs wrong la
    * def result = callonce read('./oauth2-shrop-la-admin.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/ABERD'
    And request {templates: {"APPLICATION_SUBMITTED":"la admin"}}
    When method POST
    Then status 403

  Scenario: Invalid LA
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/BOBBY_LAD'
    And request {templates: {"APPLICATION_SUBMITTED":"la admin"}}
    When method POST
    Then status 400
    And match $.error.errors[0].message contains 'Invalid.laShortCode'

  Scenario: Invalid Template name
    * def result = callonce read('./oauth2-shrop-la-admin.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'messages/localAuthorities/SHROP'
    And request {templates: {"APPLICATION_SCHMACLICATION":"la admin"}}
    When method POST
    Then status 400
    And match $.error.errors[0].message contains 'InvalidFormat.templates'
