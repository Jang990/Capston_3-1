package com.esummary.crawler.login.exception;

/**
 * 이러닝 세션과 ID가 일치하지 않음
 */
public class MismatchedELearningSessionAndID extends RuntimeException {
    public MismatchedELearningSessionAndID() {
        super();
    }

    public MismatchedELearningSessionAndID(String message) {
        super(message);
    }

    public MismatchedELearningSessionAndID(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchedELearningSessionAndID(Throwable cause) {
        super(cause);
    }
}
