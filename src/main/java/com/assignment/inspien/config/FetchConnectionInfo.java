package com.assignment.inspien.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

public class FetchConnectionInfo {

    private static final String SECRET_PATH = "src/main/resources/application-secret.yaml";

    private static String apiUrl;
    private static String authUsername;
    private static String authPassword;
    private static String name;
    private static String phone;
    private static String email;

    public static void main(String[] args) throws Exception {
        loadApplicantInfo();

        String requestBody = String.format(
                "{\"NAME\":\"%s\",\"PHONE_NUMBER\":\"%s\",\"E_MAIL\":\"%s\"}",
                name, phone, email
        );

        HttpURLConnection conn = (HttpURLConnection) URI.create(apiUrl).toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        String auth = authUsername + ":" + authPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        System.out.println("응답 코드: " + responseCode);

        Scanner scanner = new Scanner(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8
        );
        StringBuilder responseBody = new StringBuilder();
        while (scanner.hasNextLine()) {
            responseBody.append(scanner.nextLine());
        }
        scanner.close();

        System.out.println("응답 원문:");
        System.out.println(responseBody);

        if (responseCode < 200 || responseCode >= 300) {
            return;
        }

        String json = responseBody.toString();

        String applicantKey = extractJsonValue(json, "APPLICANT_KEY");
        String orderTbConnRaw = extractJsonObject(json, "ORDER_TB_CONN");
        String shipmentTbConnRaw = extractJsonObject(json, "SHIPMENT_TB_CONN");
        String ftpConnRaw = extractJsonObject(json, "FTP_CONN");
        String sampleData = extractJsonValue(json, "SAMPLE_DATA");

        System.out.println("\n=== APPLICANT_KEY ===");
        System.out.println(applicantKey);

        System.out.println("\n=== ORDER_TB_CONN (복호화 시도) ===");
        tryDecryptEachField(orderTbConnRaw);

        System.out.println("\n=== SHIPMENT_TB_CONN (복호화 시도) ===");
        tryDecryptEachField(shipmentTbConnRaw);

        System.out.println("\n=== FTP_CONN (복호화 시도) ===");
        tryDecryptEachField(ftpConnRaw);

        System.out.println("\n=== SAMPLE_DATA (디코딩, EUC-KR) ===");
        if (sampleData != null) {
            byte[] decoded = Base64.getDecoder().decode(sampleData);
            System.out.println(new String(decoded, "EUC-KR"));
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadApplicantInfo() throws Exception {
        Yaml yaml = new Yaml();
        try (FileInputStream fis = new FileInputStream(SECRET_PATH)) {
            Map<String, Object> data = yaml.load(fis);
            apiUrl = String.valueOf(data.get("EAI_API_URL"));
            authUsername = String.valueOf(data.get("EAI_API_AUTH_USERNAME"));
            authPassword = String.valueOf(data.get("EAI_API_AUTH_PASSWORD"));
            name = String.valueOf(data.get("EAI_APPLICANT_NAME"));
            phone = String.valueOf(data.get("EAI_APPLICANT_PHONE"));
            email = String.valueOf(data.get("EAI_APPLICANT_EMAIL"));
        }
    }

    private static void tryDecryptEachField(String jsonObjectRaw) {
        if (jsonObjectRaw == null) {
            System.out.println("(필드 없음)");
            return;
        }
        String[] pairs = jsonObjectRaw.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length < 2) continue;
            String key = kv[0].replaceAll("[\"{}\\s]", "");
            String value = kv[1].replaceAll("[\"{}\\s]", "");
            if (value.isEmpty()) continue;
            try {
                String decrypted = AesDecryptUtil.decrypt(value, phone);
                System.out.println(key + " = " + decrypted);
            } catch (Exception e) {
                System.out.println(key + " (복호화 실패, 원문 출력) = " + value);
            }
        }
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static String extractJsonObject(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\\{([^}]*)\\}";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : null;
    }
}