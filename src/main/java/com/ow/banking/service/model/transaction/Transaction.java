package com.ow.banking.service.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderClassName = "Builder")
public class Transaction {
    private String destinationAccountNumber;
    private String destinationName;
    private Integer txnAmount;
    private LocalDate txnDate;
    private String valueDate;
    private String reference;
    private String category;
}