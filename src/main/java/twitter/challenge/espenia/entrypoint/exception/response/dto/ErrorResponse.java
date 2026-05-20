package twitter.challenge.espenia.entrypoint.exception.response.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import twitter.challenge.espenia.core.exception.BaseAPIException;

@Getter
@Setter
public class ErrorResponse {
    private final int status;
    private final String message;
    private final String error;
    private Object cause;

    public ErrorResponse(HttpStatus status, String error, String message) {
        this.status = status.value();
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(String error, String message, int status) {
        this.message = message;
        this.error = error;
        this.status = status;
    }

    public ErrorResponse(BaseAPIException baseHttpException) {
        this.status = baseHttpException.getStatus().value();
        this.message = baseHttpException.getMessage();
        this.error = baseHttpException.getError();
    }

    @JsonCreator
    public ErrorResponse(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("error") String error,
            @JsonProperty("cause") Object cause) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.cause = cause;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorResponse other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getStatus() != other.getStatus()) {
                return false;
            } else {
                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }
                Object this$error = this.getError();
                Object other$error = other.getError();
                if (this$error == null) {
                    return other$error == null;
                } else return this$error.equals(other$error);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ErrorResponse;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getStatus();
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        return result;
    }
}
