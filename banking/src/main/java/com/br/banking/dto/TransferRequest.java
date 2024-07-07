package com.br.banking.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TransferRequest {
    String fromAccountNumber;
    String toAccountNumber;
    BigDecimal amount;
    String currency;

}
