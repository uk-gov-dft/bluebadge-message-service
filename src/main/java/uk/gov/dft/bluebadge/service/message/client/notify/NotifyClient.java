package uk.gov.dft.bluebadge.service.message.client.notify;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

@Component
@Slf4j
public class NotifyClient {

  private final NotificationClient client;
  private final NotifyTemplates notifyTemplates;

  @Autowired
  public NotifyClient(NotificationClient client, NotifyTemplates notifyTemplates) {
    this.client = client;
    this.notifyTemplates = notifyTemplates;
  }

  public UUID emailMessage(MessageDetails messageDetails, UUID messageRef) {
    log.debug("Sending an email message to Notify. Template:{}", messageDetails.getTemplate());
    String notifyTemplate = notifyTemplates.getNotifyTemplate(messageDetails.getTemplate());

    if (null == notifyTemplate) {
      Error error = new Error();
      error.setMessage("Unknown message template: " + messageDetails.getTemplate());
      log.warn(
          "Failed to send an email with Notify. Unknown template:{}", messageDetails.getTemplate());
      throw new BadRequestException(error);
    }

    try {
      SendEmailResponse emailResponse =
          client.sendEmail(
              notifyTemplate,
              messageDetails.getEmailAddress(),
              messageDetails.getAttributes(),
              messageRef.toString());
      return emailResponse.getNotificationId();
    } catch (NotificationClientException e) {
      log.warn("Failed to send an email with Notify.", e);
      Error error = new Error();
      error.setMessage("Notify exception: " + e.getMessage());
      throw new BadRequestException(error);
    }
  }
}
