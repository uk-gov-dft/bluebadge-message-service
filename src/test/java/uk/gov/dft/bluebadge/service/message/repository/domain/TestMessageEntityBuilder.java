package uk.gov.dft.bluebadge.service.message.repository.domain;

import java.util.UUID;

public class TestMessageEntityBuilder {

  public static final UUID TEST_BBB_REF_UUID = UUID.randomUUID();
  public static final UUID TEST_NOTIFY_REF_UUID = UUID.randomUUID();
  public static final String TEST_TEMPLATE = "TEST_TEMPLATE";

  public static MessageEntity.MessageEntityBuilder full() {
    return MessageEntity.builder()
        .template(TEST_TEMPLATE)
        .bbbReference(TEST_BBB_REF_UUID)
        .notifyReference(TEST_NOTIFY_REF_UUID);
  }
}
