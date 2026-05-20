package twitter.challenge.espenia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

public class ControllerTest extends IntegrationTest {
  @Autowired protected TestRestTemplate testRestTemplate;

  protected final ObjectMapper objectMapper;

  @Autowired
  public ControllerTest(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  protected <T> RequestEntity<T> getDefaultRequestEntity() {
    HttpHeaders headers = new HttpHeaders();
    return new RequestEntity<>(headers, HttpMethod.GET, null);
  }

  protected <T> T readFile(String filePath, Class<T> classType) throws IOException {
    return objectMapper.readValue(new File(filePath), classType);
  }
}
