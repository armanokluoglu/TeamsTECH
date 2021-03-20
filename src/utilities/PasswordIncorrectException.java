package utilities;

public class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException() {
        super();
    }

    public PasswordIncorrectException(String msg) {
        super(msg);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
