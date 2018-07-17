package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

@Service
@Transactional
@Slf4j
public class MessageService {

  private final MessageRepository repository;
  private final NotifyClient client;

  @Autowired
  MessageService(MessageRepository repository, NotifyClient client) {
    this.repository = repository;
    this.client = client;
  }

  public MessageEntity sendMessage(MessageDetails messageDetails) {
    log.debug("Sending message for template {}", messageDetails.getTemplate());

    UUID messageRef = UUID.randomUUID();
    UUID notifyRef = client.emailMessage(messageDetails, messageRef);

    MessageEntity entity =
        MessageEntity.builder()
            .template(messageDetails.getTemplate())
            .bbbReference(messageRef)
            .notifyReference(notifyRef)
            .build();

    repository.createMessage(entity);
    return entity;
  }
}
