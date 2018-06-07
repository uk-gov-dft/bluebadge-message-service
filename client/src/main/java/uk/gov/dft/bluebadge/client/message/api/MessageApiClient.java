package uk.gov.dft.bluebadge.client.message.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.client.message.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.message.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.model.message.UserResponse;

import static uk.gov.dft.bluebadge.client.message.api.MessageApiClient.Endpoints.*;

@Slf4j
@Service
public class MessageApiClient {

  static class Endpoints {
    static final String SEND_EMAIL_ENDPOINT = "/messages/send-email";
    static final String REMOVE_EMAIL_LINK_ENDPOINT = "/messages/{guid}";
    static final String GET_USER_BY_GUID = "/messages/user?guid={guid}";
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
  public User sendEmail(User user) {
    Assert.notNull(user, "must be set");

    HttpEntity<User> request = new HttpEntity<>(user);

      UserResponse response =
          restTemplateFactory
              .getInstance()
              .postForObject(
                  serviceConfiguration.getUrlPrefix() + SEND_EMAIL_ENDPOINT,
                  request,
                  UserResponse.class);
      return response.getData();
  }

  public void removeEmailLink(String guid) {
    Assert.notNull(guid, "must be set");

    String uri =
        UriComponentsBuilder.fromUriString(
                serviceConfiguration.getUrlPrefix() + REMOVE_EMAIL_LINK_ENDPOINT)
            .build()
            .toUriString();

    restTemplateFactory.getInstance().delete(uri, guid);
  }

  public User getUserByGUID(String guid) {
    Assert.notNull(guid, "must be set");
    UserResponse response =
        restTemplateFactory
            .getInstance()
            .getForEntity(
                serviceConfiguration.getUrlPrefix() + GET_USER_BY_GUID, UserResponse.class, guid)
            .getBody();
    return response.getData();
  }
}
