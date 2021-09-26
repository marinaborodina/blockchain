package exceptions;

public class InvalidPublicKeyException extends Exception {

    public InvalidPublicKeyException() {
        super("Public Key too short!");
    }
}
