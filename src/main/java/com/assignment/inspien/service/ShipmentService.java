package com.assignment.inspien.service;

import com.assignment.inspien.converter.ShipmentConverter;
import com.assignment.inspien.domain.Order;
import com.assignment.inspien.domain.Shipment;
import com.assignment.inspien.repository.OrderRepository;
import com.assignment.inspien.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;

    @Value("${eai.applicant.key}")
    private String applicantKey;

    @Transactional
    public void processShipment() {
        List<Order> pendingOrders = orderRepository.findByApplicantKeyAndStatus(applicantKey, "N");

        if (pendingOrders.isEmpty()) {
            log.info("처리할 미발송 주문 없음");
            return;
        }

        log.info("미발송 주문 {}건 처리 시작", pendingOrders.size());

        List<Shipment> shipments = ShipmentConverter.toShipments(pendingOrders);
        shipmentRepository.saveAll(shipments);

        pendingOrders.forEach(order -> order.setStatus("Y"));
        orderRepository.saveAll(pendingOrders);

        log.info("운송 적재 완료 {}건", shipments.size());
    }
}