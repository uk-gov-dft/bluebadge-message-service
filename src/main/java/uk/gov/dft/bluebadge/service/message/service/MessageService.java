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
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.service.notify.NotificationClientException;

@Service
@Transactional
@Slf4j
public class MessageService {

  private final NotifyClient client;
  private final NotifyTemplates dftNotifyTemplates;
  private final SecretsManager secretsManager;

  @Autowired
  MessageService(
      NotifyClient client, NotifyTemplates notifyTemplates, SecretsManager secretsManager) {
    this.client = client;
    this.dftNotifyTemplates = notifyTemplates;
    this.secretsManager = secretsManager;
  }

  public MessageEntity sendMessage(MessageDetails messageDetails) {
    if (StringUtils.hasText(messageDetails.getLaShortCode())) {
      try {
        return sendLAMessage(messageDetails);
      } catch (FailedLaMessageException e) {
        log.debug(
            "Failed to send message using LA notify. Reverting to Dft Notify. Error:{}",
            e.getMessage());
      }
    }

    return sendDftMessage(messageDetails);
  }

  private MessageEntity sendLAMessage(MessageDetails messageDetails) {
    NotifyProfile laNotifyProfile =
        secretsManager.retrieveLANotifyProfile(messageDetails.getLaShortCode());

    if (null == laNotifyProfile
        || null == laNotifyProfile.getTemplate(messageDetails.getTemplate())) {
      log.debug(
          "No Notify profile found for LA {} and template {}",
          messageDetails.getLaShortCode(),
          messageDetails.getTemplate());
      throw new FailedLaMessageException("No Notify profile found");
    }

    String notifyTemplate = laNotifyProfile.getTemplate(messageDetails.getTemplate());
    try {
      UUID messageRef = UUID.randomUUID();
      UUID notifyRef =
          client.laEmailMessage(
              laNotifyProfile.getApiKey(), notifyTemplate, messageDetails, messageRef);
      log.info("Successfully sent LA message. BB Ref:{}, Notify ref:{}", messageRef, notifyRef);
      return persistMessage(messageDetails, messageRef, notifyRef);
    } catch (Exception e) {
      log.error(
          "Failed to send email from LA ({}) notify account. Template ID:{}. Error:{}",
          messageDetails.getLaShortCode(),
          notifyTemplate,
          e.getMessage(),
          e);
      throw new FailedLaMessageException("Failed to send LA message");
    }
  }

  private MessageEntity sendDftMessage(MessageDetails messageDetails) {
    log.debug("Sending DfT message for template {}", messageDetails.getTemplate());

    String notifyTemplate = dftNotifyTemplates.getNotifyTemplate(messageDetails.getTemplate());

    if (null == notifyTemplate) {
      throw new IllegalStateException(
          "No configured template for template name:" + messageDetails.getTemplate());
    }

    try {
      UUID messageRef = UUID.randomUUID();
      UUID notifyRef = client.dftEmailMessage(notifyTemplate, messageDetails, messageRef);
      log.info("Successfully sent DFT message. BB Ref:{}, Notify ref:{}", messageRef, notifyRef);

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
    return MessageEntity.builder()
        .template(messageDetails.getTemplate().name())
        .bbbReference(messageRef)
        .notifyReference(notifyRef)
        .build();
  }

  private class FailedLaMessageException extends RuntimeException {
    private FailedLaMessageException(String message) {
      super(message);
    }
  }
}
