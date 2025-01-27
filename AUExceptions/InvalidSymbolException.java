package AUExceptions;

public class InvalidSymbolException extends Exception { 
    public InvalidSymbolException(){
        super();
    }
    public InvalidSymbolException(String errorMessage) {
        super(errorMessage);
    }
}