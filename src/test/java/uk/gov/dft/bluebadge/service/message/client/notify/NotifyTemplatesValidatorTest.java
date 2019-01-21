package uk.gov.dft.bluebadge.service.message.client.notify;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import uk.gov.dft.bluebadge.service.message.service.TemplateName;

public class NotifyTemplatesValidatorTest {
  NotifyTemplatesValidator validator = new NotifyTemplatesValidator();
  private Map<String, String> templateMap;

  @Before
  public void setup() {
    templateMap =
        ImmutableMap.of(
            TemplateName.NEW_USER.name(), "nu",
            TemplateName.RESET_PASSWORD.name(), "rp",
            TemplateName.PASSWORD_RESET_SUCCESS.name(), "prs",
            TemplateName.APPLICATION_SUBMITTED.name(), "as");
  }

  @Test
  public void valid() {
    NotifyTemplates notifyTemplates = new NotifyTemplates();
    notifyTemplates.setTemplates(templateMap);

    Errors errors = new BeanPropertyBindingResult(notifyTemplates, "notifyTemplates");
    validator.validate(notifyTemplates, errors);
    assertThat(errors.getAllErrors()).isEmpty();
  }

  @Test
  public void noTemplates() {
    NotifyTemplates notifyTemplates = new NotifyTemplates();
    notifyTemplates.setTemplates(Collections.emptyMap());

    Errors errors = new BeanPropertyBindingResult(notifyTemplates, "notifyTemplates");
    validator.validate(notifyTemplates, errors);

    assertThat(errors.getAllErrors()).isNotEmpty();
    assertThat(errors.getErrorCount()).isGreaterThan(1);
  }

  @Test
  public void missingSingle() {
    templateMap =
        ImmutableMap.of(
            TemplateName.NEW_USER.name(), "nu",
            TemplateName.RESET_PASSWORD.name(), "rp",
            TemplateName.PASSWORD_RESET_SUCCESS.name(), "prs");
    NotifyTemplates notifyTemplates = new NotifyTemplates();
    notifyTemplates.setTemplates(templateMap);

    Errors errors = new BeanPropertyBindingResult(notifyTemplates, "notifyTemplates");
    validator.validate(notifyTemplates, errors);

    assertThat(errors.getAllErrors()).isNotEmpty();
    assertThat(errors.getErrorCount()).isEqualTo(1);
  }
}
