package uk.gov.dft.bluebadge.client.message.httpclient;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return super.hasError(response);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    super.handleError(response);
  }
}
