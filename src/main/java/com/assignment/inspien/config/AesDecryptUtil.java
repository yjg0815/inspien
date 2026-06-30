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
            byte[] hash = sha1.digest(phoneNumber.getBytes("UTF-8"));
            return Arrays.copyOf(hash, 16);
        } catch (Exception e) {
            throw new RuntimeException("Key 생성 실패", e);
        }
    }

    public static String decrypt(String encryptedBase64, String phoneNumber) {
        try {
            byte[] keyBytes = generateKey(phoneNumber);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패: " + e.getMessage(), e);
        }
    }
}