package uk.gov.dft.bluebadge.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** UuidResponse */
@Validated
public class UuidResponse extends CommonResponse {
  @JsonProperty("data")
  private UuidResponseData data = null;

  public UuidResponse data(UuidResponseData data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   *
   * @return data
   */
  @ApiModelProperty(value = "")
  @Valid
  public UuidResponseData getData() {
    return data;
  }

  public void setData(UuidResponseData data) {
    this.data = data;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UuidResponse uuidResponse = (UuidResponse) o;
    return Objects.equals(this.data, uuidResponse.data) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UuidResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
