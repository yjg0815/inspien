package com.assignment.inspien.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ORDER_TB")
@IdClass(Order.OrderId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @jakarta.persistence.Id
    @Column(name = "ORDER_ID")
    private String orderId;

    @jakarta.persistence.Id
    @Column(name = "APPLICANT_KEY")
    private String applicantKey;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ITEM_ID")
    private String itemId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "STATUS")
    private String status;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderId implements Serializable {
        private String orderId;
        private String applicantKey;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderId)) return false;
            OrderId that = (OrderId) o;
            return Objects.equals(orderId, that.orderId) && Objects.equals(applicantKey, that.applicantKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, applicantKey);
        }
    }
}