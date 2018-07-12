package uk.gov.dft.bluebadge.service.message.repository;

import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

@Component
public class MessageRepository {
  @Getter private SqlSession session;

  public MessageRepository(SqlSession session) {
    this.session = session;
  }

  public int createMessage(MessageEntity messageEntity) {
    return session.insert("createMessage", messageEntity);
  }
}
