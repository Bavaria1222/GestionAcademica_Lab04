package org.example.Exeption;

public class NoDataException extends Exception {
    public NoDataException() {
    }

    public NoDataException(String msg) {
        super(msg);
    }
}
