package com.frade.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Encryptor {

    private static final String ENC_SALT = "sha256encsalt";

    // 비밀번호 암호화
    public static String encrypt(String text)
            throws NoSuchAlgorithmException {

        MessageDigest md =
                MessageDigest.getInstance("SHA-256");

        text = text + ENC_SALT;

        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    // 입력한 비밀번호와 암호화된 비밀번호 비교
    public static boolean matches(
            String inputPw,
            String encryptedPw)
            throws NoSuchAlgorithmException {

        String inputEncryptedPw =
                encrypt(inputPw);

        if(inputEncryptedPw.equals(encryptedPw)) {
            return true;
        }else {
            return false;
        }
    }

    private static String bytesToHex(byte[] cs) {

        StringBuilder sb = new StringBuilder();

        for(byte b : cs) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}