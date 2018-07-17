package uk.gov.dft.bluebadge.service.message.repository.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.Test;

public class MessageEntityTest {
  @Test
  public void whenAllFieldsSet_thenOk() {
    UUID uuid = UUID.randomUUID();
    MessageEntity result =
        MessageEntity.builder().notifyReference(uuid).bbbReference(uuid).template("stuff").build();
    assertThat(result).isNotNull();
    assertThat(result.getTemplate()).isEqualTo("stuff");
    assertThat(result.getBbbReference()).isEqualTo(uuid);
  }

  @Test(expected = NullPointerException.class)
  public void whenTemplateFieldNull_thenAssert() {
    MessageEntity.builder()
        .bbbReference(UUID.randomUUID())
        .notifyReference(UUID.randomUUID())
        .build();
  }

  @Test(expected = NullPointerException.class)
  public void whenBBBRefFieldNull_thenAssert() {
    MessageEntity.builder().template("stuff").notifyReference(UUID.randomUUID()).build();
  }

  @Test(expected = NullPointerException.class)
  public void whenNotifyRefFieldNull_thenAssert() {
    MessageEntity.builder().template("stuff").bbbReference(UUID.randomUUID()).build();
  }
}
