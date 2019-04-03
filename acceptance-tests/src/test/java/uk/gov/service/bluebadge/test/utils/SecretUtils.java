package uk.gov.service.bluebadge.test.utils;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SecretUtils {

  Logger log = LoggerFactory.getLogger(this.getClass());

  static class SecretPayload {
    private String apiKey;
    private Map<String, String> templates = new HashMap<>();

    public SecretPayload() {}

    public String getApiKey() {
      return apiKey;
    }

    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    public Map<String, String> getTemplates() {
      return templates;
    }

    public void setTemplates(Map<String, String> templates) {
      this.templates = templates;
    }
  }

  private final AWSSecretsManager awsSecretsManager;
  private final ObjectMapper objectMapper;
  private final String env;

  public SecretUtils() {

    this.awsSecretsManager = AWSSecretsManagerClientBuilder.defaultClient();
    objectMapper = new ObjectMapper();
    env = System.getenv("bb_env") == null ? "ci" : System.getenv("bb_env");
  }

  public boolean secretExistsForLa(String la) throws IOException {
    return null != getSecretForLa(la);
  }

  public SecretPayload getSecretForLa(String la) throws IOException {
    String secretName = String.format("%s/notify/%s", env, la);
    String secret;
    GetSecretValueRequest getSecretValueRequest =
        new GetSecretValueRequest().withSecretId(secretName);
    GetSecretValueResult getSecretValueResult;
    try {
      getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);
    } catch (ResourceNotFoundException exception) {
      return null;
    }

    if (getSecretValueResult == null) {
      return null;
    }

    if (getSecretValueResult.getSecretString() != null) {
      secret = getSecretValueResult.getSecretString();
      return objectMapper.readValue(secret, SecretPayload.class);
    }
    return null;
  }

  public String getApiKeyForLa(String la) throws IOException {
    return getSecretForLa(la).getApiKey();
  }

  public String getTemplateKeyForLa(String la, String template) throws IOException {
    return getSecretForLa(la).getTemplates().get(template);
  }
}
