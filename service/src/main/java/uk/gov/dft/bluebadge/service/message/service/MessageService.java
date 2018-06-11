package uk.gov.dft.bluebadge.service.message.service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.service.message.repository.MessageRepository;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;
import uk.gov.dft.bluebadge.service.message.service.domain.UserEntity;
import uk.gov.dft.bluebadge.service.message.service.exception.BlueBadgeBusinessException;

@Service
public class MessageService {

  private final MessageRepository repository;

  @Autowired
  MessageService(MessageRepository repository) {
    this.repository = repository;
  }

  public void sendPasswordResetEmail(PasswordResetEntity entity) {
    entity.setUuid(UUID.randomUUID());
    repository.createPasswordReset(entity);
  }

  public int removeEmailLink(String guid) {
    return 1;
  }

  public User getUserByGUID(String guid) {
    return new User().userId(1).guid("guid");
  }

  /**
   * Apply business validation (non bean).
   *
   * @param userEntity User Entity bean.
   * @return List of errors or null if validation ok.
   */
  /*  private List<ErrorErrors> nonBeanValidation(UserEntity userEntity) {
    if (StringUtils.isEmpty(userEntity.getEmailAddress())) {
      // Already sorted in bean validation.
      return null;
    }
    List<ErrorErrors> errorsList = null;

    if (!validEmailFormat(userEntity.getEmailAddress())) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("emailAddress")
          .message("Pattern.user.emailAddress")
          .reason("Not a valid email address.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
    }

    return errorsList;
  }

  boolean validEmailFormat(String emailAddress) {
    Matcher matcher = pattern.matcher(emailAddress);
    return matcher.find();
  }*/
}
