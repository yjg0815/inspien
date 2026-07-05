package com.assignment.inspien.service;

import com.assignment.inspien.apiPayload.code.error.OrderErrorCode;
import com.assignment.inspien.apiPayload.exception.ExceptionHandler;
import com.assignment.inspien.domain.Order;
import com.assignment.inspien.dto.OrderGroupDto;
import com.assignment.inspien.mapper.OrderConverter;
import com.assignment.inspien.mapper.OrderXmlParser;
import com.assignment.inspien.receiver.OrderDbReceiver;
import com.assignment.inspien.receiver.ReceiptFtpService;
import com.assignment.inspien.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderXmlParser orderXmlParser;
    private final OrderValidator orderValidator;
    private final OrderDbReceiver orderDbReceiver;
    private final ReceiptFtpService receiptFtpService;

    @Value("${eai.applicant.key}")
    private String applicantKey;

    @Transactional
    public void processOrder(String rawXml) {
        log.info("[Sender] 주문 요청 수신");

        List<OrderGroupDto> groups = parseXml(rawXml);
        orderValidator.validate(groups);
        log.info("[Mapper] XML 파싱 및 데이터 변환 완료 - {}건", groups.stream().mapToInt(g -> g.getItems().size()).sum());

        List<Order> orders = OrderConverter.toOrders(groups, applicantKey);
        orderDbReceiver.receive(orders);
        log.info("[Receiver-DB] ORDER_TB 적재 완료 - {}건", orders.size());

        receiptFtpService.sendReceipt(orders);
        log.info("[Receiver-FTP] 영수증 파일 전송 완료");
    }

    private List<OrderGroupDto> parseXml(String rawXml) {
        try {
            return orderXmlParser.parse(rawXml);
        } catch (Exception e) {
            log.warn("주문 XML 파싱 실패", e);
            throw new ExceptionHandler(OrderErrorCode.INVALID_XML);
        }
    }


}