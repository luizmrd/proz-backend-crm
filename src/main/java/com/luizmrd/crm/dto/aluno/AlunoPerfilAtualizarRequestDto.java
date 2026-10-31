package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.enuns.SexoEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AlunoPerfilAtualizarRequestDto(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O CPF é obrigatório")
        String cpf,
        @NotNull(message = "A data de nascimento é obrigatória")
        LocalDate dataNascimento,
        @NotNull(message = "O sexo é obrigatório")
        SexoEnum sexo,
        @NotBlank(message = "O telefone é obrigatório")
        String telefone,
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email
){
}
