package com.assignment.inspien.apiPayload.exception;

import com.assignment.inspien.apiPayload.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private ErrorCode code;
}