package com.assignment.inspien.scheduler;

import com.assignment.inspien.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipmentScheduler {

    private final ShipmentService shipmentService;

    @Scheduled(fixedDelayString = "${eai.batch.shipment-interval-ms}")
    public void run() {
        try {
            log.info("운송 배치 시작");
            shipmentService.processShipment();
            log.info("운송 배치 종료");
        } catch (Exception e) {
            log.error("운송 배치 실패", e);
        }
    }
}