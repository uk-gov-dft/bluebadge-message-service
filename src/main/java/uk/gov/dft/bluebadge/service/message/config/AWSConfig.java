package uk.gov.dft.bluebadge.service.message.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.common.service.SecretsAdapter;
import uk.gov.dft.bluebadge.service.message.service.NotifyProfile;

@Configuration
public class AWSConfig {
  @Bean
  SecretsAdapter<NotifyProfile> secretsManager() {
    return new SecretsAdapter<>(
        NotifyProfile.class, AWSSecretsManagerClientBuilder.defaultClient());
  }
}
