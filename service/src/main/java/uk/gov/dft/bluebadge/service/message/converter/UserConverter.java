package uk.gov.dft.bluebadge.service.message.converter;

import uk.gov.dft.bluebadge.model.message.User;
import uk.gov.dft.bluebadge.service.message.service.domain.UserEntity;

public class UserConverter implements BiConverter<UserEntity, User> {

  @Override
  public UserEntity convertToEntity(User model) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(model.getUserId());
    userEntity.setGuid(model.getGuid());
    return userEntity;
  }

  @Override
  public User convertToModel(UserEntity userEntity) {
    return new User().userId(userEntity.getUserId()).userId(userEntity.getUserId());
  }
}
