package com.esummary.auth.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException() {
        super();
    }
    public DuplicateNicknameException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateNicknameException(String message) {
        super(message);
    }
    public DuplicateNicknameException(Throwable cause) {
        super(cause);
    }
}