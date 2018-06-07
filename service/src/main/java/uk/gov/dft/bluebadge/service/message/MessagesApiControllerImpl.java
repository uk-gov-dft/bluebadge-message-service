package uk.gov.dft.bluebadge.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.model.message.UserId;
import uk.gov.dft.bluebadge.model.message.UserResponse;
import uk.gov.dft.bluebadge.service.message.controller.MessagesApi;
import uk.gov.dft.bluebadge.service.message.converter.UserConverter;
import uk.gov.dft.bluebadge.service.message.service.MessageService;
import uk.gov.dft.bluebadge.service.message.service.domain.UserEntity;

@Controller
public class MessagesApiControllerImpl implements MessagesApi {

  private MessageService service;
  private UserConverter userConverter = new UserConverter();
  private ObjectMapper objectMapper;
  private HttpServletRequest request;

  @SuppressWarnings("unused")
  @Autowired
  public MessagesApiControllerImpl(
      ObjectMapper objectMapper, HttpServletRequest request, MessageService service) {
    this.objectMapper = objectMapper;
    this.request = request;
    this.service = service;
  }

  @Override
  public Optional<ObjectMapper> getObjectMapper() {
    return Optional.ofNullable(objectMapper);
  }

  @Override
  public Optional<HttpServletRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  /**
   * Sends a message.
   *
   * @param userId User to send the message to.
   * @return The created user with id populated.
   */
  @Override
  public ResponseEntity<Void> messagesPost(
      @ApiParam(value = "") @Valid @RequestBody UserId userId) {
    UserResponse userResponse = new UserResponse();

    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(userId.getUserId());
    if (service.sendEmail(userEntity) == 0) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Override
  public ResponseEntity<Void> messagesGuidDelete(
      @ApiParam(value = "GUID of the user we want to remove", required = true) @PathVariable("guid")
          String guid) {
    if (service.removeEmailLink(guid) == 0) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserResponse> messagesGuidUserGet(
      @NotNull
          @ApiParam(value = "guid of the user we want to retrieve", required = true)
          @Valid
          @RequestParam(value = "guid", required = true)
          String guid) {
    User user = service.getUserByGUID(guid);
    if (user == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }
    UserResponse userResponse = new UserResponse().data(user);
    return ResponseEntity.ok(userResponse);
  }
}
