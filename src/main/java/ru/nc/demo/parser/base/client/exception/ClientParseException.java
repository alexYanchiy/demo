package ru.nc.demo.parser.base.client.exception;

public class ClientParseException extends Exception {
    private static final long serialVersionUID = 2112694646463822830L;

    public ClientParseException() {
    }

    public ClientParseException(String message) {
        super(message);
    }

    public ClientParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientParseException(Throwable cause) {
        super(cause);
    }

}
