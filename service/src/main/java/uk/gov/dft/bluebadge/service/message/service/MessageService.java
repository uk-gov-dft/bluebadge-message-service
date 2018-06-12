package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;

@Service
public class MessageService {

  private final MessageRepository repository;

  @Autowired
  MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public UUID sendPasswordResetEmail(PasswordResetEntity entity) {
    entity.setUuid(UUID.randomUUID());
    repository.createPasswordReset(entity);
    return entity.getUuid();
  }
}
