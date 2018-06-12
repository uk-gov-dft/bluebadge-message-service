package uk.gov.dft.bluebadge.client.message.api;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import uk.gov.dft.bluebadge.client.message.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.message.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.message.User;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageApiClientTest {

  @Autowired MessageApiClient messageApiClient;

  private MockRestServiceServer mockServer;

  @Before
  @Autowired
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplateFactory.getInstance());
  }

  @Autowired private ServiceConfiguration serviceConfiguration;

  @Autowired private RestTemplateFactory restTemplateFactory;

  @Test
  public void sendPasswordResetEmail() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/messages/send***REMOVED***-email";
    mockServer
            .expect(method(HttpMethod.POST))
            .andExpect(requestTo(requestUrl))
            .andRespond(
                    withSuccess(
                            "{\"apiVersion\":null,\"context\":null,\"id\":null,"
                                    + "\"method\":null,\"errors\":null,\"data\":{" +
                                    "\"uuid\": \"eb76ab8e-c91c-44a9-af2b-4bd248b56241\"}}",
                            MediaType.APPLICATION_JSON));

    User user = new User();
    user.setId(-2);
    user.setName("abc");
    user.setEmailAddress("dd@ff.com");
    UUID uuid = messageApiClient.sendPasswordResetEmail(user);
    Assert.assertEquals("Got uuid back", uuid.toString(), "eb76ab8e-c91c-44a9-af2b-4bd248b56241");

    mockServer.verify();

  }
}
