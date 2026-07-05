package com.assignment.inspien.receiver;

import com.assignment.inspien.apiPayload.code.error.OrderErrorCode;
import com.assignment.inspien.apiPayload.exception.ExceptionHandler;
import com.assignment.inspien.domain.Order;
import com.assignment.inspien.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDbReceiver {

    private final OrderRepository orderRepository;

    public void receive(final List<Order> orders) {
        try {
            orderRepository.saveAll(orders);
        } catch (Exception e) {
            log.error("[Receiver-DB] ORDER_TB 적재 실패", e);
            throw new ExceptionHandler(OrderErrorCode.DB_INSERT_FAILED);
        }
    }
}