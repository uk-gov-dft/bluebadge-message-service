package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

@Service
@Transactional
@Slf4j
public class MessageService {

  private final MessageRepository repository;

  @Autowired
  MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public MessageEntity sendMessage(MessageDetails messageDetails) {
    log.debug("Sending message for template {}", messageDetails.getTemplate());

    MessageEntity entity =
        MessageEntity.builder()
            .template(messageDetails.getTemplate())
            .uuid(UUID.randomUUID())
            .build();

    repository.createMessage(entity);
    return entity;
  }
}
