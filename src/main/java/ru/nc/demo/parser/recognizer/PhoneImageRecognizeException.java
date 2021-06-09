package ru.nc.demo.parser.recognizer;

public class PhoneImageRecognizeException extends Exception {

    public PhoneImageRecognizeException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "PhoneImageRecognizeException [" + getMessage() + "]";
    }

}
