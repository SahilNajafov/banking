package com.br.banking.controller;

import com.br.banking.dto.TransferRequest;
import com.br.banking.service.TransferService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferController {
    TransferService transferService;

    @PostMapping("/do")
    @ResponseStatus(HttpStatus.OK)
    public String transfer(@RequestBody TransferRequest transferRequest) {
        String fin = SecurityContextHolder.getContext().getAuthentication().getName();
        return transferService.transfer(fin, transferRequest);
    }
}
