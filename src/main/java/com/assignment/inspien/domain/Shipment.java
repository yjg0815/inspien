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
@Table(name = "SHIPMENT_TB")
@IdClass(Shipment.ShipmentId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @jakarta.persistence.Id
    @Column(name = "SHIPMENT_ID")
    private String shipmentId;

    @jakarta.persistence.Id
    @Column(name = "APPLICANT_KEY")
    private String applicantKey;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "ITEM_ID")
    private String itemId;

    @Column(name = "ADDRESS")
    private String address;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentId implements Serializable {
        private String shipmentId;
        private String applicantKey;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ShipmentId)) return false;
            ShipmentId that = (ShipmentId) o;
            return Objects.equals(shipmentId, that.shipmentId) && Objects.equals(applicantKey, that.applicantKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shipmentId, applicantKey);
        }
    }
}