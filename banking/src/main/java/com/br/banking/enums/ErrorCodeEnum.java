package com.br.banking.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCodeEnum {
    UNKNOWN_ERROR(1000, "unknown error!"),
    USER_NOT_FOUND(1001, "can not find user!"),
    VALIDATION_ERROR(1002, "is not valid!"),
    ALREADY_REGISTERED_ERROR(1003, "user already registered!"),
    TAKEN_EMAIL_ERROR(1004, "email already taken!"),
    BAD_CREDENTIALS_ERROR(1005, "change the credentials!"),
    ACCOUNT_NOT_FOUND(1006,"can not find account!"),
    SOURCE_ACCOUNT_NOT_FOUND(1007,"Invalid source account number!"),
    DESTINATION_ACCOUNT_NOT_FOUND(1008,"Invalid destination account number!"),
    NONE_ACTIVE_ACCOUNT(1009,"account is not active!"),
    BAD_TRANSFER_ERROR(1010,"not possible transfer!"),
    INSUFFICIENT_AMOUNT_ERROR(1011,"insufficient amount!"),
    ILLEGAL_ARGUMENT_ERROR(1012,"has been passed an illegal or inappropriate argument!");

    Integer code;
    String message;

}
