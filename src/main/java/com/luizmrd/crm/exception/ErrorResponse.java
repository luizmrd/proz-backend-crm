package com.luizmrd.crm.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    String message;
    Integer status;
}
