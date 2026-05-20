package twitter.challenge.espenia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import twitter.challenge.espenia.infra.config.ObjectMapperConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = twitter.challenge.espenia.Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"SCOPE_SUFFIX = integration_test"})
@WireMockTest(httpPort = 8092)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class WireMockIntegrationTest {

  protected final ObjectMapper objectMapper;

  protected WireMockIntegrationTest() {
    this.objectMapper = ObjectMapperConfig.getDefaultObjectMapper();
  }
}
