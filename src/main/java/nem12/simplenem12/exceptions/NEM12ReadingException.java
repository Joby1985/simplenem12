package nem12.simplenem12.exceptions;

public class NEM12ReadingException extends Exception {

    public NEM12ReadingException(String message) {
        this(message, null);
    }

    public NEM12ReadingException(String message, Throwable e) {
        super(message, e);
    }
}
