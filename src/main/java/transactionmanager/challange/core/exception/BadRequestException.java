package transactionmanager.challange.core.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseAPIException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, String error) {
        super(message, HttpStatus.BAD_REQUEST, error);
    }
}
