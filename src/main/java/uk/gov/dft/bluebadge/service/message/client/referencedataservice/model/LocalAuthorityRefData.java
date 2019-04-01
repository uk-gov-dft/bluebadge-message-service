package uk.gov.dft.bluebadge.service.message.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LocalAuthorityRefData extends ReferenceData {

  @JsonProperty("metaData")
  private LocalAuthorityMetaData localAuthorityMetaData = null;

  @JsonIgnore
  public BigDecimal getBadgeCost() {
    return getLocalAuthorityMetaData().map(LocalAuthorityMetaData::getBadgeCost).orElse(null);
  }

  public Optional<LocalAuthorityMetaData> getLocalAuthorityMetaData() {
    return Optional.ofNullable(localAuthorityMetaData);
  }

  public boolean getPaymentsEnabled() {
    return getLocalAuthorityMetaData()
        .map(LocalAuthorityMetaData::getPaymentsEnabled)
        .orElse(false);
  }

  @Data
  public static class LocalAuthorityMetaData implements Serializable {
    private String issuingAuthorityShortCode;
    private String issuingAuthorityName;
    private String contactUrl;
    private String differentServiceSignpostUrl;
    private BigDecimal badgeCost;
    private Boolean paymentsEnabled;
  }
}
