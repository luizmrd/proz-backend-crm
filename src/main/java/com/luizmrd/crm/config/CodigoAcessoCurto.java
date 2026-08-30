package com.luizmrd.crm.config;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;


@Component
public class CodigoAcessoCurto {

    private static final String CARACTERES = "QAZWSXEDCRFVTGBYHNUJMKOLP23456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public String gerar(int tamanho){
        StringBuilder codigo = new StringBuilder(tamanho);


        for (int i = 0; i < tamanho; i++) {
            int indice = RANDOM.nextInt(CARACTERES.length());
            codigo.append(CARACTERES.charAt(indice));
        }
        return codigo.toString();
    }
}
