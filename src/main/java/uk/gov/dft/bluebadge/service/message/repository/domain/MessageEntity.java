package uk.gov.dft.bluebadge.service.message.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class MessageEntity {
  @NonNull private String template;
  @NonNull private UUID bbbReference;
  @NonNull private UUID notifyReference;
}
