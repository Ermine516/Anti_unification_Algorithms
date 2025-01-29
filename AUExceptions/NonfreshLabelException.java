package AUExceptions;
public class NonfreshLabelException extends Exception { 

    public NonfreshLabelException() {
        super();
    }
    public NonfreshLabelException(String errorMessage) {
        super(errorMessage);
    }
}