package transactionmanager.challange.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import transactionmanager.challange.infra.config.ObjectMapperConfig;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = transactionmanager.challange.Application.class,
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
