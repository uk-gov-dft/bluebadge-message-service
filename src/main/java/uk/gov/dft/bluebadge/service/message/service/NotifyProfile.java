package uk.gov.dft.bluebadge.service.message.service;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotifyProfile {
  @NotBlank private String apiKey;
  @NotNull private Map<TemplateName, String> templates;

  String getTemplate(TemplateName templateName) {
    return templates.get(templateName);
  }
}
