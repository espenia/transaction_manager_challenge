package twitter.challenge.espenia.entrypoint.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import twitter.challenge.espenia.core.exception.BaseAPIException;
import twitter.challenge.espenia.entrypoint.PingController;
import twitter.challenge.espenia.entrypoint.exception.response.dto.ApiError;
import twitter.challenge.espenia.integration.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

class ControllerExceptionHandlerTest extends ControllerTest {

  private static final String NO_CAUSE_FOUND = "No cause found";
  private static final String PING = "/ping";
  private static final String ERROR = "error";

  @SpyBean private PingController pingController;

  @Autowired
  ControllerExceptionHandlerTest(final ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @Test
  void notFound() {
    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            "/fake", HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());
  }

  @Test
  void testUnhandledException() {
    // Given
    doThrow(new RuntimeException()).when(pingController).ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        responseEntity.getStatusCode().value());
  }

  @Test
  void testApiExceptionError() {
    // Given
    doThrow(new BaseAPIException(ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
        .when(pingController)
        .ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        responseEntity.getStatusCode().value());
  }

  @Test
  void testApiExceptionErrorWithCause() {
    // Given
    doThrow(
            new BaseAPIException(
                ERROR, new RuntimeException("message"), HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
        .when(pingController)
        .ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        responseEntity.getStatusCode().value());
    assertEquals("message", responseEntity.getBody().getCause());
  }

  @Test
  void testApiExceptionErrorWithBlankCause() {
    // Given
    doThrow(
            new BaseAPIException(
                ERROR, new RuntimeException("    "), HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
        .when(pingController)
        .ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        responseEntity.getStatusCode().value());
    assertEquals(NO_CAUSE_FOUND, responseEntity.getBody().getCause());
  }

  @Test
  void testApiExceptionErrorWithNullCause() {
    // Given
    doThrow(new BaseAPIException(ERROR, HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
        .when(pingController)
        .ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        responseEntity.getStatusCode().value());
    assertEquals(NO_CAUSE_FOUND, responseEntity.getBody().getCause());
  }

  @Test
  void testApiExceptionWarn() {
    // Given
    doThrow(new BaseAPIException("warn", HttpStatus.BAD_REQUEST, "warn"))
        .when(pingController)
        .ping();

    // When
    ResponseEntity<ApiError> responseEntity =
        this.testRestTemplate.exchange(
            PING, HttpMethod.GET, this.getDefaultRequestEntity(), ApiError.class);

    // Then
    assertEquals(
        HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());
  }
}
