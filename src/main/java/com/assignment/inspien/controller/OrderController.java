package com.assignment.inspien.controller;

import com.assignment.inspien.apiPayload.ApiResponse;
import com.assignment.inspien.apiPayload.code.success.OrderSuccessCode;
import com.assignment.inspien.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ApiResponse<Void>> createOrder(@RequestBody String rawXml) {
        orderService.processOrder(rawXml);
        return ResponseEntity.ok(ApiResponse.ofNoting(OrderSuccessCode.CREATE_ORDER));
    }
}