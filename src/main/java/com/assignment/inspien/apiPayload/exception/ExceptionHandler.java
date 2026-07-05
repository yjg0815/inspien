package com.assignment.inspien.apiPayload.exception;

import com.assignment.inspien.apiPayload.code.ErrorCode;

public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(ErrorCode errorCode) {
        super(errorCode);
    }
}