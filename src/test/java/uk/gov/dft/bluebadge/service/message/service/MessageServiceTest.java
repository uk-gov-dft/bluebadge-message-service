package uk.gov.dft.bluebadge.service.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

public class MessageServiceTest {

  public static final TemplateName TEST_TEMPLATE = TemplateName.NEW_USER;
  private MessageService service;

  @Mock private MessageRepository repository;
  @Mock private NotifyClient client;
  @Mock private NotifyTemplates notifyTemplates;
  @Mock private SecretsManager secretsManager;
  private NotifyProfile testNotifyProfile;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new MessageService(repository, client, notifyTemplates, secretsManager);

    testNotifyProfile =
        NotifyProfile.builder()
            .apiKey("la notify api key")
            .templates(ImmutableMap.of(TEST_TEMPLATE, "la notify template id"))
            .build();
  }

  @Test
  @SneakyThrows
  public void sendMessage_dftMessage() {
    when(repository.createMessage(any(MessageEntity.class))).thenReturn(1);
    UUID notifyRef = UUID.randomUUID();
    when(client.emailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(notifyTemplates.getNotifyTemplate(TEST_TEMPLATE)).thenReturn("test template");

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    //    messageDetails.setMessageAttributes();

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(repository).createMessage(any(MessageEntity.class));
    verify(client).emailMessage("test template", messageDetails, result.getBbbReference());
  }

  @Test
  @SneakyThrows
  public void sendMessage_laMessage() {
    when(repository.createMessage(any(MessageEntity.class))).thenReturn(1);
    UUID notifyRef = UUID.randomUUID();
    when(client.emailMessage(any(), any(), any(), any())).thenReturn(notifyRef);
    when(secretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(testNotifyProfile);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(repository).createMessage(any(MessageEntity.class));
    verifyZeroInteractions(notifyTemplates);
    verify(client, never()).emailMessage(any(), any(), any());
    verify(client)
        .emailMessage(
            eq("la notify api key"), eq("la notify template id"), eq(messageDetails), any());
  }

  @Test
  @SneakyThrows
  public void sendMessage_whenErrorInLAMessage_thenDftMessageSent() {
    when(repository.createMessage(any(MessageEntity.class))).thenReturn(1);
    when(client.emailMessage(any(), any(), any(), any()))
        .thenThrow(new RuntimeException("test error"));
    UUID notifyRef = UUID.randomUUID();
    when(client.emailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(notifyTemplates.getNotifyTemplate(TEST_TEMPLATE)).thenReturn("test template");
    when(secretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(testNotifyProfile);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(repository).createMessage(any(MessageEntity.class));
    verify(client)
        .emailMessage(
            eq("la notify api key"), eq("la notify template id"), eq(messageDetails), any());
    verify(client).emailMessage(eq("test template"), eq(messageDetails), any());
  }

  // Should never happen due to validation on start up.
  @Test(expected = RuntimeException.class)
  @SneakyThrows
  public void whenUnknownTemplate_thenException() {
    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TemplateName.APPLICATION_SUBMITTED);
    when(notifyTemplates.getNotifyTemplate(TemplateName.APPLICATION_SUBMITTED)).thenReturn(null);
    service.sendMessage(messageDetails);
    verifyZeroInteractions(client);
  }
}
