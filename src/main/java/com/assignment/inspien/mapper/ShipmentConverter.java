package com.assignment.inspien.mapper;

import com.assignment.inspien.domain.Order;
import com.assignment.inspien.domain.Shipment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShipmentConverter {

    public static List<Shipment> toShipments(final List<Order> orders) {
        List<Shipment> shipments = new ArrayList<>();
        int sequence = 0;
        for (Order order : orders) {
            shipments.add(toShipment(order, sequence++));
        }
        return shipments;
    }

    private static Shipment toShipment(final Order order, final int sequence) {
        return Shipment.builder()
                .shipmentId(generateShipmentId(sequence))
                .applicantKey(order.getApplicantKey())
                .orderId(order.getOrderId())
                .itemId(order.getItemId())
                .address(order.getAddress())
                .build();
    }

    private static String generateShipmentId(final int sequence) {
        char alphabet = (char) ('A' + (sequence % 26));
        int number = (int) (System.currentTimeMillis() % 1000);
        return alphabet + String.format("%03d", (number + sequence) % 1000);
    }
}