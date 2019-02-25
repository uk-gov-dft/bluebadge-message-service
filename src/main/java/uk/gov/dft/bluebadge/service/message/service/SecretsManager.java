package uk.gov.dft.bluebadge.service.message.service;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

  private static final String LA_NOTIFY_KEY = "%s/notify/%s";

  private final AWSSecretsManager awsSecretsManager;
  private final String secretEnv;
  private final ObjectMapper objectMapper;

  @Autowired
  SecretsManager(
      AWSSecretsManager awsSecretsManager,
      @Value("${blue-badge.notify.secretEnv}") String secretEnv) {
    this.awsSecretsManager = awsSecretsManager;
    this.secretEnv = secretEnv;
    objectMapper = new ObjectMapper();
  }

  public NotifyProfile retrieveLANotifyProfile(String la) {
    Assert.hasText(la, "LA short code is not set");
    log.info("Fetching Notify profile keys for LA:{}", la);
    return getSecret(String.format(LA_NOTIFY_KEY, secretEnv, la));
  }

  @SneakyThrows
  private NotifyProfile getSecret(String secretName) {
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
      log.debug("Secret for key:{}, {}", secretName, secret);
      NotifyProfile laNotifyProfile = objectMapper.readValue(secret, NotifyProfile.class);
      log.debug("Notify profile for key:{}, {}", secretName, laNotifyProfile);
      return laNotifyProfile;
    }

    log.error("Retrieved value from AWS Secret Manager is not a string.");
    return null;
  }
}