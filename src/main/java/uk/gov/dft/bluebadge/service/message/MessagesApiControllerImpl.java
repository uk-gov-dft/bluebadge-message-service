package uk.gov.dft.bluebadge.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.common.service.exception.ServiceException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponse;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponseData;
import uk.gov.dft.bluebadge.service.message.generated.controller.MessagesApi;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.MessageService;

@RestController
@Slf4j
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

  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @Override
  public ResponseEntity<UuidResponse> sendMessage(
      @ApiParam(value = "The template, email address and message attributes", required = true)
          @RequestBody
          @Valid
          MessageDetails messageDetails) {
    log.info("Sending message. Details:{}", messageDetails);
    MessageEntity messageEntity = service.sendMessage(messageDetails);
    UuidResponse response = new UuidResponse();
    UuidResponseData data = new UuidResponseData();
    data.setUuid(messageEntity.getBbbReference().toString());
    response.setData(data);
    return ResponseEntity.ok(response);
  }
}
