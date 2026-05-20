package twitter.challenge.espenia.core.exception;

import org.springframework.http.HttpStatus;


public class NotFoundException extends BaseAPIException {

    public NotFoundException() {
        this("Entity not found.");
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message, String error) {
        super(message, HttpStatus.NOT_FOUND, error);
    }
}
