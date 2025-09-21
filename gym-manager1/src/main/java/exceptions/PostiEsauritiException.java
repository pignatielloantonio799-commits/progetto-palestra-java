package exceptions;

public class PostiEsauritiException extends Exception {

    public PostiEsauritiException(String message) {
        super(message);
    }
    
    public PostiEsauritiException(String message, Throwable cause) {
        super(message, cause);
    }
}