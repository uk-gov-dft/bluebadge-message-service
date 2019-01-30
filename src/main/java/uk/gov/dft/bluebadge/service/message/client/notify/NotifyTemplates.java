package uk.gov.dft.bluebadge.service.message.client.notify;

import java.util.Map;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.service.message.service.TemplateName;

@Setter
@Validated
public class NotifyTemplates {
  private Map<String, String> templates;

  public String getNotifyTemplate(TemplateName templateName) {
    return templates.get(templateName.name());
  }
}
