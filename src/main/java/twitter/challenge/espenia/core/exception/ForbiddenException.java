package twitter.challenge.espenia.core.exception;

import org.springframework.http.HttpStatus;


public class ForbiddenException extends BaseAPIException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, String error) {
        super(message, HttpStatus.FORBIDDEN, error);
    }
}
