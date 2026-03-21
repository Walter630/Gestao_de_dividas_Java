package com.Gestao_de_Contas.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;


public class TokenHashUtil {
    private TokenHashUtil() {}

    public static String hashUtil(String token) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            String sha3Hex = new String(Hex.encode(hash));
            return sha3Hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
