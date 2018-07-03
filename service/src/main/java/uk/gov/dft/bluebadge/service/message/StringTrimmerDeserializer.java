package uk.gov.dft.bluebadge.service.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class StringTrimmerDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(final JsonParser p, final DeserializationContext ctxt)
      throws IOException {

    String result = StringDeserializer.instance.deserialize(p, ctxt);
    return StringUtils.trimToNull(result);
  }
}
