package com.scheduling.api.infra.errors.bussines;

import com.scheduling.api.infra.errors.HttpBaseException;

public class ConflictRecordException extends HttpBaseException {
    public ConflictRecordException(String message) {
        super(message, Status.CONFLICT);
    }
}
