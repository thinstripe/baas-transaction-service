package com.ow.banking.bank.model.transaction;

import lombok.*;

import java.time.LocalDate;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TransferRequest {
    private String type;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String denomination;
    private Float amount;
    private String description;

    // specializations
    private LocalDate transferDate;
    private LocalDate transferStartDate;
    private String transferPeriod;
    private LocalDate transferEndDate;
    private Integer transferPayments;
}