package uk.gov.dft.bluebadge.model.message.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** MessageDetails */
@Validated
public class MessageDetails {
  @JsonProperty("template")
  private String template = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("attributes")
  @Valid
  private Map<String, String> attributes = null;

  public MessageDetails template(String template) {
    this.template = template;
    return this;
  }

  /**
   * Message template name
   *
   * @return template
   */
  @ApiModelProperty(example = "RESET_PASSWORD", required = true, value = "Message template name")
  @NotNull
  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public MessageDetails emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * email address.
   *
   * @return emailAddress
   */
  @ApiModelProperty(
    example = "rob.worthington@norealserver.com",
    required = true,
    value = "email address."
  )
  @NotNull
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public MessageDetails attributes(Map<String, String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public MessageDetails putAttributesItem(String key, String attributesItem) {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    this.attributes.put(key, attributesItem);
    return this;
  }

  /**
   * Get attributes
   *
   * @return attributes
   */
  @ApiModelProperty(example = "{\"name\":\"Bob\"}", value = "")
  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageDetails messageDetails = (MessageDetails) o;
    return Objects.equals(this.template, messageDetails.template)
        && Objects.equals(this.emailAddress, messageDetails.emailAddress)
        && Objects.equals(this.attributes, messageDetails.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(template, emailAddress, attributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessageDetails {\n");

    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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
