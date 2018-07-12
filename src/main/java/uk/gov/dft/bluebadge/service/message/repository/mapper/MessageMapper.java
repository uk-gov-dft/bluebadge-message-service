package uk.gov.dft.bluebadge.service.message.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;

@Mapper
@SuppressWarnings("unused")
public interface MessageMapper {
  void createMessage(MessageEntity messageEntity);
}
