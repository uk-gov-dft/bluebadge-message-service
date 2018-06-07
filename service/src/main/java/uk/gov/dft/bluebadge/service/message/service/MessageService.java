package uk.gov.dft.bluebadge.service.message.service;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.service.message.service.domain.UserEntity;
import uk.gov.dft.bluebadge.service.message.service.exception.BlueBadgeBusinessException;

@Service
public class MessageService {

  private static final Pattern pattern =
      Pattern.compile(
          "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
          Pattern.CASE_INSENSITIVE);

  // TODO: There will be a repository later
  /*
    @Autowired MessageService(MessageRepository repository) {
      this.repository = repository;
    }
  */

  // TODO: In usermanagement-service we use UserEntity but in MessageService we do not have DB
  /**
   * Create user entity.
   *
   * @param userEntity user to send a essage to.
   * @return number of messages sent.
   * @throws BlueBadgeBusinessException if validation fails.
   */
  public int sendEmail(UserEntity userEntity) {
    /*List<ErrorErrors> businessErrors = nonBeanValidation(userEntity);
    if (null != businessErrors) {
      throw new UserEntityValidationException(businessErrors);
    }*/
    //return repository.createUser(userEntity);
    return 1; // TODO: (Not sending yet)
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
