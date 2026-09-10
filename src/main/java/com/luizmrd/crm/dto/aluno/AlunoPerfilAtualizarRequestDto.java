package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.enuns.SexoEnum;

import java.time.LocalDate;

public record AlunoPerfilAtualizarRequestDto(
String nome,
String cpf,
LocalDate dataNacimento,
SexoEnum sexo,
String telefone,
String email
){
}
