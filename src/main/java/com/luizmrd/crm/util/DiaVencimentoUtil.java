package com.luizmrd.crm.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
public class DiaVencimentoUtil {
    public static LocalDate montarDataVencimento(Integer diaVencimento){
        if(diaVencimento <1 || diaVencimento > 31){
            throw new IllegalArgumentException("Dia de vencimento inválido. Deve ser entre 1 e 31.");
        }

        YearMonth mesAtual = YearMonth.now();

        int ultimoDiaDoMes = mesAtual.lengthOfMonth();
        int diaReal = Math.min(diaVencimento, ultimoDiaDoMes);
        LocalDate dataVencimento = mesAtual.atDay(diaReal);

        return dataVencimento;
    }

}
