package uk.gov.dft.bluebadge.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dft.bluebadge.model.message.generated.PasswordResetRequest;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponse;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponseData;
import uk.gov.dft.bluebadge.service.message.generated.controller.MessagesApi;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;
import uk.gov.dft.bluebadge.service.message.service.MessageService;

@Controller
public class MessagesApiControllerImpl implements MessagesApi {

  private MessageService service;
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
   * @param passwordReset User to send the message to.
   * @return The created user with id populated.
   */
  @Override
  public ResponseEntity<UuidResponse> sendPasswordChangeEmail(
      @ApiParam(value = "The user that needs an email link sending.", required = true)
          @Valid
          @RequestBody
          PasswordResetRequest passwordReset) {

    PasswordResetEntity entity = new PasswordResetEntity();
    entity.setUserId(passwordReset.getUserId());
    UUID createdUuid = service.sendPasswordResetEmail(entity);
    UuidResponse response = new UuidResponse();
    UuidResponseData data = new UuidResponseData();
    data.setUuid(createdUuid.toString());
    response.setData(data);
    return ResponseEntity.ok(response);
  }
}
