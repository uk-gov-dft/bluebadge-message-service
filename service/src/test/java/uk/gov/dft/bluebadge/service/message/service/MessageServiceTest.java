package uk.gov.dft.bluebadge.service.message.service;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;

import java.util.UUID;

public class MessageServiceTest {

  private MessageService service;

  @Mock private MessageRepository repository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new MessageService(repository);
  }

  @Test
  public void sendPasswordResetEmail() {
    PasswordResetEntity passwordResetEntity = new PasswordResetEntity();
    when(repository.createPasswordReset(passwordResetEntity)).thenReturn(1);

    UUID result = service.sendPasswordResetEmail(passwordResetEntity);
    Assert.notNull(result, "UUID not created.");
    Assert.hasLength(result.toString(), "UUID not created.");
  }
}
