package uk.gov.dft.bluebadge.service.message.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.message.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.repository.domain.TestMessageEntityBuilder;

@RunWith(SpringRunner.class)
@Transactional
public class MessageRepositoryTest extends ApplicationContextTests {

  @Autowired MessageRepository messageRepository;

  @Test
  public void whenAllFieldsSet_thenPersisted() {
    MessageEntity messageEntity = TestMessageEntityBuilder.full().build();
    int resultCount = messageRepository.createMessage(messageEntity);

    assertThat(resultCount).isEqualTo(1);

    MessageEntity persisted = retrieveMessageEntityByUUID(messageEntity.getUuid());
    assertThat(persisted).isNotNull();
    assertThat(persisted.getUuid()).isEqualTo(messageEntity.getUuid());
    assertThat(persisted.getTemplate()).isEqualTo(messageEntity.getTemplate());
  }

  private MessageEntity retrieveMessageEntityByUUID(UUID uuid) {
    return messageRepository.getSession().selectOne("selectMessageByUuid", uuid);
  }
}
