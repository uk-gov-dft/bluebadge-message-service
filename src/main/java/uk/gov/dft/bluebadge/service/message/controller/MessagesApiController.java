package uk.gov.dft.bluebadge.service.message.controller;

import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.api.common.CommonResponseHandler;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponse;
import uk.gov.dft.bluebadge.model.message.generated.UuidResponseData;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.MessageService;
import uk.gov.dft.bluebadge.service.message.service.NotifyProfile;

@RestController
@RequestMapping("messages")
@Slf4j
@CommonResponseHandler
public class MessagesApiController {

  private MessageService service;

  @SuppressWarnings("unused")
  @Autowired
  public MessagesApiController(MessageService service) {
    this.service = service;
  }

  @PostMapping
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

  @PostMapping("/localAuthorities/{laShortCode}")
  @PreAuthorize(
      "hasAuthority('PERM_UPDATE_NOTIFY_LA_SECRET') and @securityUtils.isAuthorisedLACode(#laShortCode)")
  public ResponseEntity<Void> updateLocalNotifySecret(
      @Valid @RequestBody NotifyProfile profile, @PathVariable String laShortCode) {
    service.createOrUpdateNotifyProfile(laShortCode.toUpperCase(), profile);
    return ResponseEntity.ok().build();
  }
}
