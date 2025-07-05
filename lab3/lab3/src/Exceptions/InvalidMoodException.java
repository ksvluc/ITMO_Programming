package Exceptions;

public class InvalidMoodException extends RuntimeException {
    public InvalidMoodException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Недопустимое настроение: " + super.getMessage();
    }
}
