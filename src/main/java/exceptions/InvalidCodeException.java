package exceptions;

public class InvalidCodeException extends Exception {

    public InvalidCodeException() {
        super("Invalid Code! Please Insert a valid Python-Code!");
    }
}
