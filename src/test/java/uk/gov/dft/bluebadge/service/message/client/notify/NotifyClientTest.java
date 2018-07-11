package uk.gov.dft.bluebadge.service.message.client.notify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.SendEmailResponse;

@RunWith(MockitoJUnitRunner.class)
public class NotifyClientTest {

  @Mock private NotificationClient mockClient;
  @Mock private NotifyTemplates mockTemplates;
  @Mock private SendEmailResponse mockResponse;
  private NotifyClient notifyClient;
  private MessageDetails messageDetails;
  private ImmutableMap<String, String> messageAttributes;

  @Before
  public void setup() {
    notifyClient = new NotifyClient(mockClient, mockTemplates);
    messageDetails = new MessageDetails();
    messageDetails.setTemplate("TEST_TEMPLATE_1");
    messageDetails.setEmailAddress("bob@bob.com");
    messageAttributes = ImmutableMap.of("name", "bob");
    messageDetails.setAttributes(messageAttributes);
  }

  @SneakyThrows
  @Test
  public void whenAllOk_thenMessageSent() {
    UUID ourRef = UUID.randomUUID();
    UUID notifyRef = UUID.randomUUID();
    when(mockTemplates.getNotifyTemplate("TEST_TEMPLATE_1")).thenReturn("notify_template");
    when(mockClient.sendEmail(any(), any(), any(), any())).thenReturn(mockResponse);
    when(mockResponse.getNotificationId()).thenReturn(notifyRef);

    UUID confirmationRef = notifyClient.emailMessage(messageDetails, ourRef);
    assertThat(confirmationRef).isEqualTo(notifyRef);

    verify(mockClient)
        .sendEmail(
            "notify_template",
            messageDetails.getEmailAddress(),
            messageAttributes,
            ourRef.toString());
  }

  @SneakyThrows
  @Test(expected = BadRequestException.class)
  public void whenUnknownTemplate_thenException() {
    UUID uuid = UUID.randomUUID();
    messageDetails.setTemplate("not a valid template");
    notifyClient.emailMessage(messageDetails, uuid);
    verify(mockClient, never()).sendEmail(any(), any(), any(), any());
  }
}
