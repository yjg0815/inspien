package com.assignment.inspien.apiPayload.code.error;

import com.assignment.inspien.apiPayload.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    INVALID_XML(HttpStatus.BAD_REQUEST, "ORDER4001", "주문 XML 형식이 올바르지 않습니다"),
    DB_INSERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER5001", "주문 DB 적재에 실패했습니다"),
    FTP_TRANSFER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ORDER5002", "영수증 FTP 전송에 실패했습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}