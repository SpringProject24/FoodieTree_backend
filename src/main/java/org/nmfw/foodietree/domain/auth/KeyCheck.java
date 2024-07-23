package org.nmfw.foodietree.domain.auth;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyCheck {
    public static void main(String[] args) {
        // Base64 인코딩된 비밀 키
        String base64Key = "3S31H9DtSpksfIgtN+u6ZgLvj7hWvCqu/oHYSUvWYUUQ/JULvniqpmqk4rqDTyEPVxl86ueE9tb064yVzJodHA==";

        // Base64로 인코딩된 비밀 키를 디코딩합니다.
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));

        // 디코딩된 비밀 키를 출력합니다.
        System.out.println("Decoded Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));

        // 비밀 키의 길이를 바이트와 비트 단위로 출력합니다.
        byte[] encodedKey = key.getEncoded();
        int keyLengthInBytes = encodedKey.length;
        int keyLengthInBits = keyLengthInBytes * 8;

        System.out.println("Key Length in Bytes: " + keyLengthInBytes);
        System.out.println("Key Length in Bits: " + keyLengthInBits);
    }
}
