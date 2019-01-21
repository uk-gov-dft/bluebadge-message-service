package uk.gov.dft.bluebadge.service.message.service;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
@Slf4j
public class SecretsManager {

  private static final String API_KEY = "%s/notify/%s/apikey";
  private static final String TEMPLATE_KEY = "%s/notify/%s/template/%s";

  private final AWSSecretsManager awsSecretsManager;
  private final String secretEnv;

  @Autowired
  SecretsManager(
      AWSSecretsManager awsSecretsManager, @Value("blue-badge.notify.secretEnv") String secretEnv) {
    this.awsSecretsManager = awsSecretsManager;
    this.secretEnv = secretEnv;
  }

  public String retrieveLANotifyApiKey(String la) {
    Assert.hasText(la, "LA short code is not set");
    return getSecret(String.format(API_KEY, secretEnv, la));
  }

  public String retrieveLANotifyTemplate(String la, TemplateName template) {
    Assert.hasText(la, "LA short code is not set");
    Assert.notNull(template, "Template is not set");
    return getSecret(String.format(TEMPLATE_KEY, secretEnv, la, template.name()));
  }

  private String getSecret(String secretName) {
    String secret;
    GetSecretValueRequest getSecretValueRequest =
        new GetSecretValueRequest().withSecretId(secretName);
    GetSecretValueResult getSecretValueResult = null;
    try {
      getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);
    } catch (ResourceNotFoundException e) {
      log.debug("The requested secret " + secretName + " was not found");
    } catch (InvalidRequestException e) {
      log.error("The request was invalid due to: " + e.getMessage());
    } catch (InvalidParameterException e) {
      log.error("The request had invalid params: " + e.getMessage());
    }

    if (getSecretValueResult == null) {
      return null;
    }

    if (getSecretValueResult.getSecretString() != null) {
      secret = getSecretValueResult.getSecretString();
      log.debug(secret);
      return secret;
    }

    log.error("Retrieved value from AWS Secret Manager is not a string.");
    return null;
  }
}
