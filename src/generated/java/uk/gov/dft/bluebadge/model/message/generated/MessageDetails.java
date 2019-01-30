package uk.gov.dft.bluebadge.model.message.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.validation.constraints.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.service.message.service.TemplateName;

/** MessageDetails */
@Validated
@Data
public class MessageDetails {
  private String laShortCode = null;

  @NotNull private TemplateName template = null;

  @Email @NotNull private String emailAddress = null;

  @JsonProperty("attributes")
  private Map<String, String> attributes = null;
}
