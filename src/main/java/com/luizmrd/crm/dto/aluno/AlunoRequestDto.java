package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.time.LocalDate;

public record AlunoRequestDto (
String nome,
String cpf,
LocalDate dataNascimento,
SexoEnum sexo,
String telefone,
String email,
Long plano,
StatusPagamentoEnum statusPagamento
){
}
