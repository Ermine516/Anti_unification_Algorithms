package AUExceptions;
public class NonGroundException extends Exception { 
    public NonGroundException() {
        super();
    }
    public NonGroundException(String errorMessage) {
        super(errorMessage);
    }
}