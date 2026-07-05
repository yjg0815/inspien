package com.assignment.inspien.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
    String getName();
}