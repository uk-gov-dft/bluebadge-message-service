package uk.gov.dft.bluebadge.service.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplatesValidator;
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
}
