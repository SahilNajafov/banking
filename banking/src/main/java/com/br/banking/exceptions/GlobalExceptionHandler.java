package com.br.banking.exceptions;

import com.br.banking.enums.ErrorCodeEnum;
import com.br.banking.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static com.br.banking.enums.ErrorCodeEnum.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomException(UserNotFoundException e) {
        return ErrorResponse.of(USER_NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInputParam(MethodArgumentTypeMismatchException e) {
        String param = e.getParameter().getParameter().getName();
        return ErrorResponse.of(VALIDATION_ERROR, param);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInputParam(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder fieldName= new StringBuilder();
        for (FieldError a:fieldErrors) {
            fieldName.append(a.getField()).append(" ");
        }
        System.out.println(fieldName);
        return ErrorResponse.of(VALIDATION_ERROR, fieldName.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(ConstraintViolationException e) {
        String fieldName = null;
        List<String> fieldNames = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolations : e.getConstraintViolations()) {
            fieldName = constraintViolations.getPropertyPath().toString();
            fieldNames.add(fieldName);
        }
        return ErrorResponse.of(VALIDATION_ERROR, fieldName);
    }

    @ExceptionHandler(AlreadyRegisteredUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRepetition(AlreadyRegisteredUserException e) {
        return ErrorResponse.of(ALREADY_REGISTERED_ERROR);
    }

    @ExceptionHandler(TakenEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTakenEmail(TakenEmailException e) {
        return ErrorResponse.of(TAKEN_EMAIL_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCredentialsIssue(BadCredentialsException e) {
        return ErrorResponse.of(BAD_CREDENTIALS_ERROR);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNoneExistence(AccountNotFoundException e) {
        return ErrorResponse.of(ACCOUNT_NOT_FOUND);
    }

    @ExceptionHandler(NoneActiveAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoneActiveAccount(NoneActiveAccountException e) {
        return ErrorResponse.of(NONE_ACTIVE_ACCOUNT);
    }

    @ExceptionHandler(ImpossibleTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotPossibleTransfer(ImpossibleTransferException e) {
        return ErrorResponse.of(BAD_TRANSFER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        return ErrorResponse.of(ILLEGAL_ARGUMENT_ERROR);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleUnknown(Exception e) {
//        return ErrorResponse.builder()
//                .code(ErrorCodeEnum.UNKNOWN_ERROR.getCode())
//                .message(ErrorCodeEnum.UNKNOWN_ERROR.getMessage())
//                .build();
//    }

}
