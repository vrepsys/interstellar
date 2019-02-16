package cc.interstellar;

public class InterstellarException extends RuntimeException {

    public InterstellarException(Throwable cause) {
        super(cause);
    }

    public InterstellarException(String message) {
        super(message);
    }
}
