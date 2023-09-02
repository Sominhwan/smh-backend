package kr.co.smh.exception;

public class RefreshTokenGrantTypeException extends RuntimeException {
    public RefreshTokenGrantTypeException(String message) {
        super(message);
    }
}