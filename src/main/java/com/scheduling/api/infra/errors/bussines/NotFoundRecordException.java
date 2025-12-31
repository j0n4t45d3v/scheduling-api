package com.scheduling.api.infra.errors.bussines;

import com.scheduling.api.infra.errors.HttpBaseException;

public class NotFoundRecordException extends HttpBaseException {
    public NotFoundRecordException(String message) {
        super(message, Status.NOT_FOUND);
    }
}
