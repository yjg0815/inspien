package com.assignment.inspien.repository;

import com.assignment.inspien.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Shipment.ShipmentId> {
}