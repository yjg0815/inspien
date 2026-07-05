package com.assignment.inspien.mapper;

import com.assignment.inspien.domain.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReceiptFileConverter {

    private static final String DELIMITER = "^";

    public static String toFileContent(final List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append(toLine(order)).append("\n");
        }
        return sb.toString();
    }

    private static String toLine(final Order order) {
        return String.join(DELIMITER,
                order.getOrderId(),
                order.getUserId(),
                order.getItemId(),
                order.getApplicantKey(),
                order.getName(),
                order.getAddress(),
                order.getItemName(),
                order.getPrice()
        );
    }
}