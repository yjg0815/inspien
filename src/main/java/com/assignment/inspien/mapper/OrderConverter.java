package com.assignment.inspien.mapper;

import com.assignment.inspien.domain.Order;
import com.assignment.inspien.dto.OrderGroupDto;
import com.assignment.inspien.dto.OrderItemDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderConverter {

    public static List<Order> toOrders(final List<OrderGroupDto> groups, final String applicantKey) {
        List<Order> orders = new ArrayList<>();
        int sequence = 0;

        for (OrderGroupDto group : groups) {
            for (OrderItemDto item : group.getItems()) {
                orders.add(toOrder(group, item, applicantKey, sequence++));
            }
        }
        return orders;
    }

    private static Order toOrder(final OrderGroupDto group, final OrderItemDto item,
                                  final String applicantKey, final int sequence) {
        return Order.builder()
                .orderId(generateOrderId(sequence))
                .applicantKey(applicantKey)
                .userId(item.getUserId())
                .name(group.getHeader().getName())
                .address(group.getHeader().getAddress())
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .status(group.getHeader().getStatus())
                .build();
    }

    private static String generateOrderId(final int sequence) {
        char alphabet = (char) ('A' + (sequence % 26));
        int number = (int) (System.currentTimeMillis() % 1000);
        return alphabet + String.format("%03d", (number + sequence) % 1000);
    }
}