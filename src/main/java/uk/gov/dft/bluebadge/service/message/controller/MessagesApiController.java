package uk.gov.dft.bluebadge.service.message.controller;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponse;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponseData;
import uk.gov.dft.bluebadge.service.message.generated.controller.MessagesApi;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.MessageService;

import javax.validation.Valid;

@RestController
@Slf4j
public class MessagesApiController implements MessagesApi {

  private MessageService service;

  @SuppressWarnings("unused")
  @Autowired
  public MessagesApiController(MessageService service) {
    this.service = service;
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
