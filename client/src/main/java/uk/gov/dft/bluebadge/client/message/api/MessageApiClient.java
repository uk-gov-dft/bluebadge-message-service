package uk.gov.dft.bluebadge.client.message.api;

import static uk.gov.dft.bluebadge.client.message.api.MessageApiClient.Endpoints.SEND_EMAIL_ENDPOINT;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.client.message.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.message.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.model.message.UuidResponse;

import java.util.UUID;

@Slf4j
@Service
public class MessageApiClient {

  static class Endpoints {
    static final String SEND_EMAIL_ENDPOINT = "/messages/send***REMOVED***-email";
  }

  @Qualifier("messageServiceConfiguration")
  private ServiceConfiguration serviceConfiguration;

  private RestTemplateFactory restTemplateFactory;

  @Autowired
  public MessageApiClient(
      ServiceConfiguration serviceConfiguration, RestTemplateFactory restTemplateFactory) {
    this.serviceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  /**
   * @param user with the user id property set to the user we want to send the email to.
   * @return a user with the given user id and the guid set.
   */
  public UUID sendPasswordResetEmail(User user) {
    Assert.notNull(user, "must be set");

    HttpEntity<User> request = new HttpEntity<>(user);

      UuidResponse response =
          restTemplateFactory
              .getInstance()
              .postForObject(
                  serviceConfiguration.getUrlPrefix() + SEND_EMAIL_ENDPOINT,
                  request,
                      UuidResponse.class);
      return UUID.fromString(response.getData().getUuid());
  }
}
