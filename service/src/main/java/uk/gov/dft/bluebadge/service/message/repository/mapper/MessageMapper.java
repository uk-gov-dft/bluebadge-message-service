package uk.gov.dft.bluebadge.service.message.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;

@Mapper
@SuppressWarnings("unused")
public interface MessageMapper {
  void createPasswordReset(PasswordResetEntity passwordResetEntity);
}
