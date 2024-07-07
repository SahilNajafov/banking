package com.br.banking.exceptions;

import com.br.banking.enums.ErrorCodeEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TakenEmailException extends RuntimeException {
    Integer code;
    String message;

    public TakenEmailException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}