package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.enuns.SexoEnum;

import java.time.LocalDate;

public record AlunoPerfilAtualizarRequestDto(
String nome,
String cpf,
LocalDate dataNascimento,
SexoEnum sexo,
String telefone,
String email
){
}
