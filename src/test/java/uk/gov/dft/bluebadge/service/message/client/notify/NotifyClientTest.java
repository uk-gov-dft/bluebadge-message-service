package uk.gov.dft.bluebadge.service.message.client.notify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.service.TemplateName;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

@RunWith(MockitoJUnitRunner.class)
public class NotifyClientTest {

  @Mock private NotificationClient mockClient;
  @Mock private SendEmailResponse mockResponse;
  private NotifyClient notifyClient;
  private MessageDetails messageDetails;
  private ImmutableMap<String, String> messageAttributes;

  @Before
  public void setup() {
    notifyClient = new NotifyClient(mockClient);
    messageDetails = new MessageDetails();
    messageDetails.setTemplate(TemplateName.NEW_USER);
    messageDetails.setEmailAddress("bob@bob.com");
    messageAttributes = ImmutableMap.of("name", "bob");
    messageDetails.setAttributes(messageAttributes);
  }

  @SneakyThrows
  @Test
  public void whenAllOk_thenMessageSent() {
    UUID ourRef = UUID.randomUUID();
    UUID notifyRef = UUID.randomUUID();
    when(mockClient.sendEmail(eq("notify template id"), any(), any(), any()))
        .thenReturn(mockResponse);
    when(mockResponse.getNotificationId()).thenReturn(notifyRef);

    UUID confirmationRef =
        notifyClient.dftEmailMessage("notify template id", messageDetails, ourRef);
    assertThat(confirmationRef).isEqualTo(notifyRef);

    verify(mockClient)
        .sendEmail(
            "notify template id",
            messageDetails.getEmailAddress(),
            messageAttributes,
            ourRef.toString());
  }

  @SneakyThrows
  @Test
  public void whenNotifyException_thenException() {
    UUID ourRef = UUID.randomUUID();
    NotificationClientException clientException = new NotificationClientException("Notify no like");
    when(mockClient.sendEmail(any(), any(), any(), any())).thenThrow(clientException);

    messageDetails.setTemplate(TemplateName.NEW_USER);
    try {
      notifyClient.dftEmailMessage("template id", messageDetails, ourRef);
      fail("No exception thrown");
    } catch (NotificationClientException e) {
      assertThat(e).isSameAs(clientException);
    }

    verify(mockClient)
        .sendEmail(
            "template id", messageDetails.getEmailAddress(), messageAttributes, ourRef.toString());
  }
}
