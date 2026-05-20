package twitter.challenge.espenia.core.exception;


public class ValidationException extends BadRequestException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String error) {
        super(message, error);
    }
}
