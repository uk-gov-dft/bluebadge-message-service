package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.service.notify.NotificationClientException;

@Service
@Transactional
@Slf4j
public class MessageService {

  private final MessageRepository repository;
  private final NotifyClient client;
  private final NotifyTemplates dftNotifyTemplates;
  private final SecretsManager secretsManager;

  @Autowired
  MessageService(
      MessageRepository repository,
      NotifyClient client,
      NotifyTemplates notifyTemplates,
      SecretsManager secretsManager) {
    this.repository = repository;
    this.client = client;
    this.dftNotifyTemplates = notifyTemplates;
    this.secretsManager = secretsManager;
  }

  public MessageEntity sendMessage(MessageDetails messageDetails) {
    String laNotifyApiKey = null;
    String laNotifyTemplate = null;
    if (StringUtils.hasText(messageDetails.getLaShortCode())) {
      laNotifyApiKey = secretsManager.retrieveLANotifyApiKey(messageDetails.getLaShortCode());
      if (StringUtils.hasText(laNotifyApiKey)) {
        laNotifyTemplate =
            secretsManager.retrieveLANotifyTemplate(
                messageDetails.getLaShortCode(), messageDetails.getTemplate());
      }
    }

    if (!StringUtils.hasText(laNotifyTemplate)) {
      return sendDftMessage(messageDetails);
    }

    try {
      UUID messageRef = UUID.randomUUID();
      UUID notifyRef =
          client.emailMessage(laNotifyApiKey, laNotifyTemplate, messageDetails, messageRef);

      return persistMessage(messageDetails, messageRef, notifyRef);
    } catch (Exception e) {
      log.error(
          "Failed to send email from LA ({}) notify account. Template ID:{}. Error:{}",
          messageDetails.getLaShortCode(),
          laNotifyTemplate,
          e.getMessage(),
          e);
      return sendDftMessage(messageDetails);
    }
  }

  private MessageEntity sendDftMessage(MessageDetails messageDetails) {
    log.debug("Sending DfT message for template {}", messageDetails.getTemplate());

    String notifyTemplate = dftNotifyTemplates.getNotifyTemplate(messageDetails.getTemplate());

    if (null == notifyTemplate) {
      throw new RuntimeException(
          "No configured template for template name:" + messageDetails.getTemplate());
    }

    try {
      UUID messageRef = UUID.randomUUID();

      UUID notifyRef = client.emailMessage(notifyTemplate, messageDetails, messageRef);

      return persistMessage(messageDetails, messageRef, notifyRef);
    } catch (NotificationClientException e) {
      log.warn("Failed to send an email with Notify. Error:{}", e.getMessage());
      Error error = new Error();
      error.setMessage("Notify exception: " + e.getMessage());
      throw new BadRequestException(error);
    }
  }

  private MessageEntity persistMessage(
      MessageDetails messageDetails, UUID messageRef, UUID notifyRef) {
    MessageEntity entity =
        MessageEntity.builder()
            .template(messageDetails.getTemplate().name())
            .bbbReference(messageRef)
            .notifyReference(notifyRef)
            .build();

    repository.createMessage(entity);
    return entity;
  }
}
