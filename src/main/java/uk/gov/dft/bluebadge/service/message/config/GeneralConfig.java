package uk.gov.dft.bluebadge.service.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplatesValidator;
import uk.gov.service.notify.Authentication;
import uk.gov.service.notify.NotificationClient;

@Configuration
public class GeneralConfig {
  @Value("${blue-badge.notify.apiKey}")
  private String apiKey;

  @Bean
  public NotificationClient notificationClient() {
    return new NotificationClient(apiKey);
  }

  @ConfigurationProperties("blue-badge.notify")
  @Bean
  @Validated
  public NotifyTemplates notifyTemplates() {
    return new NotifyTemplates();
  }

  @Bean
  public static Validator configurationPropertiesValidator() {
    return new NotifyTemplatesValidator();
  }

  public static void main(String[] args) {
    String apiKey =
        "bbb_test_key-70a18c16-bfa1-4d8c-83ac-c8d16f425ea7-74148e9f-8d19-40cd-8dc4-741934e74937";
    String auth = apiKey.substring(Math.max(0, apiKey.length() - 36));
    System.out.println(auth);

    String serviceId =
        apiKey.substring(Math.max(0, apiKey.length() - 73), Math.max(0, apiKey.length() - 37));
    System.out.println(serviceId);

    Authentication au = new Authentication();
    String token = au.create(serviceId, auth);
    System.out.println(token);
  }
}
