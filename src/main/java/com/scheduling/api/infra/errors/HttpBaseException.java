package com.scheduling.api.infra.errors;

public class HttpBaseException extends RuntimeException {

    public enum Status {
        NOT_FOUND(404);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    private final Status httpStatus;

    public HttpBaseException(String message, Status httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public Status getHttpStatus() {
        return this.httpStatus;
    }

    public int getHttpStatusCode() {
        return this.httpStatus.getCode();
    }
}
