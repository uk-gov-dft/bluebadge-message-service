package uk.gov.dft.bluebadge.service.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;

public class NotifyProfileTest {
  ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  @Test
  public void deserialise() {
    String json =
        "{\n"
            + "  \"apiKey\": \"TEST-71295ef1-7b76-42a4-9f27-a34357576a53-4857c9bf-f81a-4926-8160-d159c1e537c3\",\n"
            + "  \"templates\": {\n"
            + "    \"PASSWORD_RESET_SUCCESS\": \"cac113af-716c-45c6-9764-2548387c0832\",\n"
            + "    \"APPLICATION_SUBMITTED\": \"ef20f7c9-2459-41a6-9d52-d4dca752b41d\"\n"
            + "  }\n"
            + "}";
    NotifyProfile notifyProfile = mapper.readValue(json, NotifyProfile.class);
    assertThat(notifyProfile).isNotNull();
    assertThat(notifyProfile.getApiKey())
        .isEqualTo(
            "TEST-71295ef1-7b76-42a4-9f27-a34357576a53-4857c9bf-f81a-4926-8160-d159c1e537c3");
    assertThat(notifyProfile.getTemplates())
        .containsKeys(TemplateName.PASSWORD_RESET_SUCCESS, TemplateName.APPLICATION_SUBMITTED);
    assertThat(notifyProfile.getTemplates())
        .contains(
            entry(TemplateName.PASSWORD_RESET_SUCCESS, "cac113af-716c-45c6-9764-2548387c0832"),
            entry(TemplateName.APPLICATION_SUBMITTED, "ef20f7c9-2459-41a6-9d52-d4dca752b41d"));
  }
}
