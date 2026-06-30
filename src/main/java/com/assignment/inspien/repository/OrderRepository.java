package com.assignment.inspien.repository;

import com.assignment.inspien.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Order.OrderId> {

    List<Order> findByApplicantKeyAndStatus(String applicantKey, String status);
}