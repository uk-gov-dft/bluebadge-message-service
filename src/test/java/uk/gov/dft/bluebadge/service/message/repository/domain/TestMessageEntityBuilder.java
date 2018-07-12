package uk.gov.dft.bluebadge.service.message.repository.domain;

import java.util.UUID;

public class TestMessageEntityBuilder {

  public static final UUID TEST_UUID = UUID.randomUUID();
  public static final String TEST_TEMPLATE = "TEST_TEMPLATE";

  public static MessageEntity.MessageEntityBuilder full() {
    return MessageEntity.builder().template(TEST_TEMPLATE).uuid(TEST_UUID);
  }
}
