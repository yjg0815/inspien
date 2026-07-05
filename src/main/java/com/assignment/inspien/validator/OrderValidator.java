package com.assignment.inspien.validator;

import com.assignment.inspien.apiPayload.code.error.OrderErrorCode;
import com.assignment.inspien.apiPayload.exception.ExceptionHandler;
import com.assignment.inspien.dto.OrderGroupDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {

    public void validate(final List<OrderGroupDto> groups) {
        if (groups == null || groups.isEmpty()) {
            throw new ExceptionHandler(OrderErrorCode.INVALID_XML);
        }
        groups.forEach(this::validateGroup);
    }

    private void validateGroup(final OrderGroupDto group) {
        validateHeader(group);
        validateItems(group);
    }

    private void validateHeader(final OrderGroupDto group) {
        if (group.getHeader() == null || group.getHeader().getUserId() == null) {
            throw new ExceptionHandler(OrderErrorCode.INVALID_XML);
        }
    }

    private void validateItems(final OrderGroupDto group) {
        if (group.getItems() == null || group.getItems().isEmpty()) {
            throw new ExceptionHandler(OrderErrorCode.INVALID_XML);
        }
    }
}