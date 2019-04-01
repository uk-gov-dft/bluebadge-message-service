package uk.gov.dft.bluebadge.service.message.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class ReferenceDataResponse extends CommonResponse {

  @JsonProperty("data")
  private List<ReferenceData> data = null;

  public List<ReferenceData> getData() {
    return data;
  }

  public void setData(List<ReferenceData> data) {
    this.data = data;
  }
}
