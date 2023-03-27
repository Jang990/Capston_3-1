package com.esummary.crawler.logincrawler.exception;

/**
 * 만료된 이러닝 세션
 */
public class ExpiredELearningSession extends RuntimeException {
    public ExpiredELearningSession() {
        super();
    }

    public ExpiredELearningSession(String message) {
        super(message);
    }

    public ExpiredELearningSession(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredELearningSession(Throwable cause) {
        super(cause);
    }
}
