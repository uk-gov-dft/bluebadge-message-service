package uk.gov.dft.bluebadge.service.message.service;

import static org.springframework.util.StringUtils.hasText;

import com.google.common.collect.ImmutableMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.referencedata.ReferenceDataService;
import uk.gov.service.notify.NotificationClientException;

@Service
@Transactional
@Slf4j
public class MessageService {

  private final NotifyClient client;
  private final NotifyTemplates dftNotifyTemplates;
  private final NotifySecretsManager secretsManager;
  private final ReferenceDataService referenceDataService;

  @Autowired
  MessageService(
      NotifyClient client,
      NotifyTemplates notifyTemplates,
      NotifySecretsManager secretsManager,
      ReferenceDataService referenceDataService) {
    this.client = client;
    this.dftNotifyTemplates = notifyTemplates;
    this.secretsManager = secretsManager;
    this.referenceDataService = referenceDataService;
  }

  public MessageEntity sendMessage(MessageDetails messageDetails) {
    if (hasText(messageDetails.getLaShortCode())) {
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
    NotifyProfile laNotifyProfile;
    try {
      laNotifyProfile = secretsManager.retrieveLANotifyProfile(messageDetails.getLaShortCode());
    } catch (Exception e) {
      throw new FailedLaMessageException("No Notify profile found");
    }

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

  public void createOrUpdateNotifyProfile(String laShortCode, NotifyProfile newProfile) {
    Assert.notNull(laShortCode, "La required.");
    Assert.notNull(newProfile, "New profile required.");

    if (null == referenceDataService.getLocalAuthority(laShortCode)) {
      String reason = laShortCode + " is not a recognised Local Authority short code.";
      throw new BadRequestException(new Error().message("Invalid.laShortCode").reason(reason));
    }

    try {
      NotifyProfile oldProfile = secretsManager.retrieveLANotifyProfile(laShortCode);
      log.debug("Updating existing notify profile for {}.", laShortCode);
      secretsManager.createOrUpdateNotifyProfile(
          laShortCode, mergeNotifyProfiles(oldProfile, newProfile));
    } catch (NotFoundException e) {
      log.debug("Creating new notify profile for {}.", laShortCode);
      secretsManager.createOrUpdateNotifyProfile(laShortCode, newProfile);
    }
  }

  NotifyProfile mergeNotifyProfiles(NotifyProfile oldProfile, NotifyProfile newProfile) {
    if (null == oldProfile) {
      return newProfile;
    }

    if (null == newProfile) {
      return oldProfile;
    }

    NotifyProfile.NotifyProfileBuilder builder = NotifyProfile.builder();
    builder.apiKey(
        hasText(newProfile.getApiKey()) ? newProfile.getApiKey() : oldProfile.getApiKey());

    // A set of all templates either in AWS or in request.
    Set<TemplateName> templateKeys = new HashSet<>();
    if (newProfile.getTemplates() != null && !newProfile.getTemplates().isEmpty()) {
      templateKeys.addAll(newProfile.getTemplates().keySet());
    }
    if (oldProfile.getTemplates() != null && !oldProfile.getTemplates().isEmpty()) {
      templateKeys.addAll(oldProfile.getTemplates().keySet());
    }

    ImmutableMap.Builder<TemplateName, String> mapBuilder = ImmutableMap.builder();
    for (TemplateName templateName : templateKeys) {
      mapBuilder.put(
          templateName,
          hasText(newProfile.getTemplate(templateName))
              ? newProfile.getTemplate(templateName)
              : oldProfile.getTemplate(templateName));
    }
    return builder.templates(mapBuilder.build()).build();
  }
}
