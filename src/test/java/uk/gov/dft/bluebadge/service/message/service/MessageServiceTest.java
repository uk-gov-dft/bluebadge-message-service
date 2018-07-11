package uk.gov.dft.bluebadge.service.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

public class MessageServiceTest {

  public static final String TEST_TEMPLATE = "TEST_TEMPLATE";
  private MessageService service;

  @Mock private MessageRepository repository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new MessageService(repository, client);
  }

  @Test
  public void sendMessageTest() {
    when(repository.createMessage(any(MessageEntity.class))).thenReturn(1);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    //    messageDetails.setMessageAttributes();

    MessageEntity result = service.sendMessage(messageDetails);
    assertThat(result).isNotNull();
    assertThat(result.getUuid()).isNotNull();
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE);

    verify(repository).createMessage(any(MessageEntity.class));
  }
}
