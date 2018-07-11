package uk.gov.dft.bluebadge.service.message.client.notify;

import java.util.Map;
import lombok.Setter;

@Setter
public class NotifyTemplates {
  private Map<String, String> templates;

  public String getNotifyTemplate(String bbbTemplate) {
    return templates.get(bbbTemplate);
  }
}
