package uk.gov.dft.bluebadge.service.message.repository.domain;

import java.util.UUID;

public class PasswordResetEntity {

    private Integer userId;
    private UUID uuid;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
