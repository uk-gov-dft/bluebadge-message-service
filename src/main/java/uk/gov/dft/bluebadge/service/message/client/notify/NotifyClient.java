package uk.gov.dft.bluebadge.service.message.client.notify;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.service.notify.NotificationClient;

@Component
public class NotifyClient {

  private final NotificationClient client;

  @Autowired
  public NotifyClient(NotificationClient client) {
    this.client = client;
  }

  public void emailMessage(MessageDetails messageDetails){
    client.sendEmail(templateId, emailAddress, attributes, reference);
  }
}
