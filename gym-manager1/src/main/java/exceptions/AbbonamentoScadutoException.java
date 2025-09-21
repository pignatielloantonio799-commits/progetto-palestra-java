package exceptions;

public class AbbonamentoScadutoException extends Exception {

    public AbbonamentoScadutoException(String message) {
        super(message);
    }
    
    public AbbonamentoScadutoException(String message, Throwable cause) {
        super(message, cause);
    }
}