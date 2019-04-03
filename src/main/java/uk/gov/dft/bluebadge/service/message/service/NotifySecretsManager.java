package uk.gov.dft.bluebadge.service.message.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.service.SecretsAdapter;

@Service
@Transactional
@Slf4j
public class NotifySecretsManager {

  private static final String LA_NOTIFY_KEY = "%s/notify/%s";

  private final SecretsAdapter<NotifyProfile> secretsAdapter;
  private final String secretEnv;

  @Autowired
  NotifySecretsManager(
      SecretsAdapter<NotifyProfile> secretsAdapter,
      @Value("${blue-badge.notify.secretEnv}") String secretEnv) {
    this.secretsAdapter = secretsAdapter;
    this.secretEnv = secretEnv;
  }

  NotifyProfile retrieveLANotifyProfile(String laShortCode) {
    Assert.hasText(laShortCode, "LA short code is not set for retrieve from AWS.");
    log.debug("Fetching Notify profile keys for LA:{}", laShortCode);
    return secretsAdapter.getSecretValue(formatSecretKey(laShortCode));
  }

  void createOrUpdateNotifyProfile(String laShortCode, NotifyProfile profile) {
    Assert.hasText(laShortCode, "LA short code is not set for createOrUpdate.");
    secretsAdapter.createOrUpdateSecret(formatSecretKey(laShortCode), profile);
  }

  private String formatSecretKey(String laShortCode) {
    return String.format(LA_NOTIFY_KEY, secretEnv, laShortCode.toUpperCase());
  }
}
