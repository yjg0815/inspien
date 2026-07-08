package com.assignment.inspien.receiver;

import com.assignment.inspien.apiPayload.code.error.OrderErrorCode;
import com.assignment.inspien.apiPayload.exception.ExceptionHandler;
import com.assignment.inspien.domain.Order;
import com.assignment.inspien.mapper.ReceiptFileConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptFtpService {

    private static final DateTimeFormatter FILE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Value("${eai.ftp.host}")
    private String ftpHost;

    @Value("${eai.ftp.port}")
    private int ftpPort;

    @Value("${eai.ftp.username}")
    private String ftpUsername;

    @Value("${eai.ftp.password}")
    private String ftpPassword;

    @Value("${eai.ftp.path}")
    private String ftpPath;

    @Value("${eai.ftp.retry-count}")
    private int retryCount;

    @Value("${eai.applicant.name}")
    private String applicantName;

    public void sendReceipt(final List<Order> orders) {
        String fileName = generateFileName();
        String content = ReceiptFileConverter.toFileContent(orders);

        log.info("영수증 파일 생성 - 파일명: {}, 주문 {}건", fileName, orders.size());
        log.debug("영수증 파일 내용:\n{}", content); // DEBUG 레벨로 분리

        Exception lastException = null;

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            FTPClient ftpClient = new FTPClient();
            try {
                connect(ftpClient);
                upload(ftpClient, fileName, content);
                log.info("FTP 전송 성공 (시도 {}/{}): {}", attempt, retryCount, fileName);
                return;
            } catch (Exception e) {
                lastException = e;
                log.warn("FTP 전송 실패 (시도 {}/{}): {}", attempt, retryCount, e.getMessage());
                if (attempt < retryCount) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } finally {
                disconnect(ftpClient);
            }
        }

        log.error("FTP 전송 최종 실패 ({}회 시도): {}", retryCount, fileName, lastException);
        throw new ExceptionHandler(OrderErrorCode.FTP_TRANSFER_FAILED);
    }

    private void connect(final FTPClient ftpClient) throws Exception {
        ftpClient.setControlEncoding("EUC-KR");
        ftpClient.connect(ftpHost, ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

        if (ftpPath != null && !ftpPath.isBlank()) {
            ftpClient.changeWorkingDirectory(ftpPath);
        }
    }

    private void upload(final FTPClient ftpClient, final String fileName, final String content) throws Exception {
        try (InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            boolean success = ftpClient.storeFile(fileName, is);
            if (!success) {
                throw new IllegalStateException("FTP storeFile 실패: " + fileName);
            }
        }
    }

    private void disconnect(final FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            log.warn("FTP 연결 종료 중 오류", e);
        }
    }

    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(FILE_TIME_FORMAT);
        return "INSPIEN_" + applicantName + "_" + timestamp + ".txt";
    }
}