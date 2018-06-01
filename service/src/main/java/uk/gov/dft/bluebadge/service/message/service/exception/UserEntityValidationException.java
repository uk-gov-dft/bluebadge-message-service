package uk.gov.dft.bluebadge.service.message.service.exception;

import java.util.List;
import uk.gov.dft.bluebadge.model.message.ErrorErrors;

public class UserEntityValidationException extends BlueBadgeBusinessException {
  public UserEntityValidationException(List<ErrorErrors> errorsList) {
    super(errorsList);
  }
}
