package twitter.challenge.espenia.entrypoint.exception.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Class containing relevant information from an API call error. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

  /** -- error -- */
  private String error;

  /** -- message -- */
  private String message;

  /** -- status -- */
  private Integer status;

  /** -- cause -- */
  private String cause;

  /**
   * Creates a new instance, with provided fields.
   *
   * @param error error short description.
   * @param message full error message.
   * @param status HTTP Status.
   */
  public ApiError(String error, String message, Integer status) {
    this.error = error;
    this.message = message;
    this.status = status;
  }
}
