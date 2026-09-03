package com.luizmrd.crm.config;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CodigoHash {

    public static String gerarHash(String texto) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = algorithm.digest(texto.getBytes(StandardCharsets.UTF_8));

            // Converte os bytes para representação Hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash não encontrado", e);
        }
    }
}
