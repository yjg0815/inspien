package com.assignment.inspien.converter;

import com.assignment.inspien.domain.Order;
import com.assignment.inspien.domain.Shipment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShipmentConverter {

    public static List<Shipment> toShipments(final List<Order> orders) {
        return orders.stream()
                .map(ShipmentConverter::toShipment)
                .collect(Collectors.toList());
    }

    private static Shipment toShipment(final Order order) {
        return Shipment.builder()
                .shipmentId(generateShipmentId(order))
                .applicantKey(order.getApplicantKey())
                .orderId(order.getOrderId())
                .itemId(order.getItemId())
                .address(order.getAddress())
                .build();
    }

    private static String generateShipmentId(final Order order) {
        return "S_" + order.getOrderId();
    }
}