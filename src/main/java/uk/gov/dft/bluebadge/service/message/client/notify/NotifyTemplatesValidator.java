package uk.gov.dft.bluebadge.service.message.client.notify;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.gov.dft.bluebadge.service.message.service.TemplateName;

public class NotifyTemplatesValidator implements Validator {

  @Override
  public boolean supports(Class<?> type) {
    return type == NotifyTemplates.class;
  }

  @Override
  public void validate(Object o, Errors errors) {
    NotifyTemplates notifyTemplates = (NotifyTemplates) o;
    for (TemplateName templateName : TemplateName.values()) {
      String notifyTemplate = notifyTemplates.getNotifyTemplate(templateName);
      if (!StringUtils.hasText(notifyTemplate)) {
        errors.reject("missing.template", "Missing notify template: " + templateName.name());
      }
    }
  }
}
