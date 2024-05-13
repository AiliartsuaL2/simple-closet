package hckt.simplecloset.global.exception;

public class NotRegisteredUserAccountException extends RuntimeException {
    public NotRegisteredUserAccountException(String message) {
        super(message);
    }
}
