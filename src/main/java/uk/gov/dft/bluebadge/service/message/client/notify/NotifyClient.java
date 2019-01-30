package uk.gov.dft.bluebadge.service.message.client.notify;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

@Component
@Slf4j
public class NotifyClient {

  private final NotificationClient dftClient;

  @Autowired
  public NotifyClient(NotificationClient client) {
    this.dftClient = client;
  }

  public UUID laEmailMessage(
      String notifyApiKey, String notifyTemplate, MessageDetails messageDetails, UUID messageRef)
      throws NotificationClientException {
    log.info(
        "Sending an email message to LA ({}) Notify. Template name:{}, Notify Template:{}, BB Ref:{}",
        messageDetails.getLaShortCode(),
        messageDetails.getTemplate(),
        notifyTemplate,
        messageRef);
    NotificationClient client = new NotificationClient(notifyApiKey);
    return emailMessage(client, notifyTemplate, messageDetails, messageRef);
  }

  public UUID dftEmailMessage(String notifyTemplate, MessageDetails messageDetails, UUID messageRef)
      throws NotificationClientException {
    log.info(
        "Sending an email message to DFT Notify. Template name:{}, Notify Template:{}, BB Ref:{}",
        messageDetails.getTemplate(),
        notifyTemplate,
        messageRef);
    return emailMessage(dftClient, notifyTemplate, messageDetails, messageRef);
  }

  private UUID emailMessage(
      NotificationClient client,
      String notifyTemplate,
      MessageDetails messageDetails,
      UUID messageRef)
      throws NotificationClientException {
    SendEmailResponse emailResponse =
        client.sendEmail(
            notifyTemplate,
            messageDetails.getEmailAddress(),
            messageDetails.getAttributes(),
            messageRef.toString());
    return emailResponse.getNotificationId();
  }
}
