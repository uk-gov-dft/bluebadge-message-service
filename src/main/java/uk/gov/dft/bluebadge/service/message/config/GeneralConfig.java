package uk.gov.dft.bluebadge.service.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.service.notify.NotificationClient;

@Configuration
public class GeneralConfig {
  @Value("${notify.apiKey}")
  private String apiKey;

  @Bean
  public NotificationClient notificationClient() {
    return new NotificationClient(apiKey);
  }

  @ConfigurationProperties("notify")
  @Bean
  public NotifyTemplates notifyTemplates() {
    return new NotifyTemplates();
  }
}
