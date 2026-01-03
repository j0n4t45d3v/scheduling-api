package com.scheduling.api.domain.exceptions.offeredservice;

import com.scheduling.api.domain.exceptions.DomainException;

public class OfferedServiceInvalidException extends DomainException {
    public OfferedServiceInvalidException(String message) {
        super(message);
    }
}
