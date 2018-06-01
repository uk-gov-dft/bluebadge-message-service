package uk.gov.dft.bluebadge.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** User */
@Validated
public class User {
  @JsonProperty("userId")
  private Integer userId = null;

  @JsonProperty("guid")
  private String guid = null;

  public User userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   *
   * @return userId
   */
  @ApiModelProperty(example = "45", required = true, value = "")
  @NotNull
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public User guid(String guid) {
    this.guid = guid;
    return this;
  }

  /**
   * Get guid
   *
   * @return guid
   */
  @ApiModelProperty(example = "e61ae7be-3d2f-4f32-9aab-adc915b6b58c", value = "")
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.userId, user.userId) && Objects.equals(this.guid, user.guid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, guid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");

    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    guid: ").append(toIndentedString(guid)).append("\n");
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
