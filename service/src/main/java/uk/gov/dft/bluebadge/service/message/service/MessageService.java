package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;

@Service
@Slf4j
public class MessageService {

  private final MessageRepository repository;

  @Autowired
  MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public UUID sendPasswordResetEmail(PasswordResetEntity entity) {
    log.debug("Creating password reset email for user {}", entity.getUuid());
    entity.setUuid(UUID.randomUUID());
    repository.createPasswordReset(entity);
    return entity.getUuid();
  }
}
