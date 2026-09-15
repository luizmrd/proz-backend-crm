package com.luizmrd.crm.util;

public class ReciboUtil {

    public static String gerarCodigoRecibo() {
        return "#REC-" + System.currentTimeMillis();
    }

    public static String gerarCodigoComprovante() {
        return "#COMP-" + System.currentTimeMillis();
    }


}
