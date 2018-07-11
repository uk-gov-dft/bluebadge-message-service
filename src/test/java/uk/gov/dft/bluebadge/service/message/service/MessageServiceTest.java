package uk.gov.dft.bluebadge.service.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

public class MessageServiceTest {

  public static final String TEST_TEMPLATE = "TEST_TEMPLATE";
  private MessageService service;

  @Mock private MessageRepository repository;
  @Mock private NotifyClient client;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new MessageService(repository, client);
  }

  @Test
  public void sendMessageTest() {
    when(repository.createMessage(any(MessageEntity.class))).thenReturn(1);
    UUID notifyRef = UUID.randomUUID();
    when(client.emailMessage(any(), any())).thenReturn(notifyRef);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    //    messageDetails.setMessageAttributes();

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE);

    verify(repository).createMessage(any(MessageEntity.class));
    verify(client).emailMessage(messageDetails, result.getBbbReference());
  }
}
