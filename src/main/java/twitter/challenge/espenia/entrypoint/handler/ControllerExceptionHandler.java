package twitter.challenge.espenia.entrypoint.handler;

import org.springframework.http.HttpStatus;
import twitter.challenge.espenia.core.exception.BaseAPIException;
import twitter.challenge.espenia.entrypoint.exception.response.dto.ApiError;
import com.newrelic.api.agent.NewRelic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/** Basic handling for exceptions. */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  private static final String LOG_FLAGS = "[message:{}][error:{}][status:{}]";
  private static final String CAUSE_FLAGS = LOG_FLAGS + "[cause:[{}]]";
  private static final String NO_CAUSE_FOUND = "No cause found";

  /**
   * Handler for not found routes.
   *
   * @param ex the exception thrown when route is not found.
   * @return {@link ResponseEntity} with 404 status code and the route that was not found in the
   *     body.
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiError> noHandlerFoundException(final NoHandlerFoundException ex) {
    ApiError apiError =
        new ApiError(
            "route_not_found",
            String.format("Route %s not found", ex.getRequestURL()),
            HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  /**
   * Handler for external API exceptions.
   *
   * @param e the exception thrown during a request to external API.
   * @return {@link ResponseEntity} with status code and description provided for the handled
   *     exception.
   */
  @ExceptionHandler({BaseAPIException.class})
  protected ResponseEntity<ApiError> handleApiException(final BaseAPIException e) {
    int statusCode = e.getStatus().value();
    boolean expected = HttpStatus.INTERNAL_SERVER_ERROR.value() > statusCode;
    NewRelic.noticeError(e, expected);
    if (expected) {
      log.warn("Internal Api warn. Status Code: {}", statusCode, e);
    } else {
      log.error("Internal Api error. Status Code: {}", statusCode, e);
    }
    log.error(
        CAUSE_FLAGS,
        e.getMessage(),
        e.getError(),
        e.getStatus(),
        e.getCause() != null && StringUtils.isNotBlank(e.getCause().getMessage())
            ? e.getCause().getMessage()
            : NO_CAUSE_FOUND);

    ApiError apiError =
        new ApiError(
            e.getError(),
            e.getMessage(),
            statusCode,
            e.getCause() != null && StringUtils.isNotBlank(e.getCause().getMessage())
                ? e.getCause().getMessage()
                : NO_CAUSE_FOUND);
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }

  /**
   * Handler for internal exceptions.
   *
   * @param e the exception thrown during request processing.
   * @return {@link ResponseEntity} with 500 status code and description indicating an internal
   *     error.
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handleUnknownException(final Exception e) {
    log.error("Internal error", e);
    NewRelic.noticeError(e);

    ApiError apiError =
        new ApiError(
            "internal_error",
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(apiError.getStatus()).body(apiError);
  }
}
