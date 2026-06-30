package com.assignment.inspien.apiPayload.code.success;

import com.assignment.inspien.apiPayload.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderSuccessCode implements SuccessCode {

    CREATE_ORDER(HttpStatus.OK, "ORDER2001", "주문 생성 및 연계 처리 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}