package com.esummary.crawler.exception;

/**
 * 이러닝 세션과 ID가 일치하지 않음
 */
public class MismatchELearningSessionAndID extends RuntimeException {
    public MismatchELearningSessionAndID() {
        super();
    }

    public MismatchELearningSessionAndID(String message) {
        super(message);
    }

    public MismatchELearningSessionAndID(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchELearningSessionAndID(Throwable cause) {
        super(cause);
    }
}
