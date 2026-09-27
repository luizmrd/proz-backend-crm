package com.luizmrd.crm.handler;

import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ErrorResponse;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handlerBadRequest(BadRequestException ex){
        ErrorResponse resp = ErrorResponse.of(
                ex.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerResourceNotFound(ResourceNotFoundException ex){
        ErrorResponse resp = ErrorResponse.of(
                ex.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handlerUnauthorized(UnauthorizedException ex){
        ErrorResponse resp = ErrorResponse.of(
                ex.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handlerAccessDenied(AccessDeniedException ex){
        ErrorResponse resp = ErrorResponse.of(
                "ACESSO_NEGADO",
                "Você não tem permissão para acessar este recurso",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handlerTypeMismatch(MethodArgumentTypeMismatchException ex){
        ErrorResponse resp = ErrorResponse.of(
                "PARAMETRO_INVALIDO",
                "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }
}
