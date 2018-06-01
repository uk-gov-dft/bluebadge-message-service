package uk.gov.dft.bluebadge.service.message.service.domain;

import java.io.Serializable;
import lombok.Data;
//import org.apache.ibatis.type.Alias;

/** Bean to hold a UserEntity record. */
@Data
//@Alias("UserEntity")
public class UserEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer userId;
  private String guid;
}
