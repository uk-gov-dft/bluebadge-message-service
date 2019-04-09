@authentication-dft-user
Feature: Authenticate with the authorisation service using the password grant for a DFT Admin user

  Background:
    * url authServerUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = db.runScript('test-data.sql')

  Scenario: Obtain access token with the password grant

    * path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'ms_SHROP@la.editors.dft.gov.uk'
    * form field  ***REMOVED***
    * method post
    * status 200
    * match $.access_token == '#notnull'
    * match $.token_type == 'bearer'
    * def accessToken = response.access_token