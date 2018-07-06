package uk.gov.dft.bluebadge.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponse;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponseData;
import uk.gov.dft.bluebadge.service.message.generated.controller.MessagesApi;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.MessageService;

@Controller
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

  @Override
  public ResponseEntity<UuidResponse> sendMessage(
      @ApiParam(value = "The template, email address and message attributes", required = true)
          @RequestBody
          @Valid
          MessageDetails messageDetails) {
    //    log.info("Se");
    MessageEntity messageEntity = service.sendMessage(messageDetails);
    UuidResponse response = new UuidResponse();
    UuidResponseData data = new UuidResponseData();
    data.setUuid(messageEntity.getUuid().toString());
    response.setData(data);
    return ResponseEntity.ok(response);
  }
}
