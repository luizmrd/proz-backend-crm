package com.luizmrd.crm.dto;

import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.time.LocalDate;

public record AlunoAtualizarRequestDto(
String nome,
String cpf,
LocalDate dataNacimento,
SexoEnum sexo,
String telefone,
String email
){
}
