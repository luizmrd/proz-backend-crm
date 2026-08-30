package com.luizmrd.crm.dto;

import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlunoRequestDto (
String nome,
String cpf,
LocalDate dataNacimento,
SexoEnum sexo,
String telefone,
String email,
PlanoEntity plano,
BigDecimal valorMensal,
StatusPagamentoEnum statusPagamento
){
}
