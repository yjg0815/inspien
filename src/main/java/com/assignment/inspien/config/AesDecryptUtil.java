package com.assignment.inspien.config;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AesDecryptUtil {

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static byte[] generateKey(String phoneNumber) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hash = sha1.digest(phoneNumber.getBytes("UTF-8")); // UTF-8 변환 -> sha-1해싱(160비트 고정 변환환)
            return Arrays.copyOf(hash, 16);// 앞 16바이트 자르기(128비트)
        } catch (Exception e) {
            throw new RuntimeException("Key 생성 실패", e);
        }
    }

    public static String decrypt(String encryptedBase64, String phoneNumber) {
        try {
            byte[] keyBytes = generateKey(phoneNumber); // AES-128 키 만들기 
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64); // 암호문 base64 디코딩

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey); 
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes); // AES 복호화

            return new String(decryptedBytes, "UTF-8"); // UTF_8 변환
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패: " + e.getMessage(), e);
        }
    }
}