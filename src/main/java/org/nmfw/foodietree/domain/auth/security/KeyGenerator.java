package org.nmfw.foodietree.domain.auth.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

// 개발자 local 시크릿 키 발급 제너레이터
public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Generated Refresh Secret Key: " + encodedKey);
    }
}
