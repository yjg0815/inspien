package com.assignment.inspien.apiPayload.exception;

import com.assignment.inspien.apiPayload.ApiResponse;
import com.assignment.inspien.apiPayload.code.ErrorCode;
import com.assignment.inspien.apiPayload.code.error.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice {

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> onThrowException(GeneralException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getCode();
        log.warn("[GeneralException] {} {} - {}", request.getMethod(), request.getRequestURI(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.onFailure(errorCode));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> onThrowException(Exception e, HttpServletRequest request) {
        log.error("[UnhandledException] {} {}", request.getMethod(), request.getRequestURI(), e);
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(ApiResponse.onFailure(CommonErrorCode.INTERNAL_ERROR, e.getMessage()));
    }
}