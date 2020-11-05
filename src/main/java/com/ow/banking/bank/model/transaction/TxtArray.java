package com.ow.banking.bank.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(builderClassName = "Builder")
public class TxtArray {
    private Integer txnAmount;
    private LocalDate txnDate;
    private String custAccountType;
    private String custAccountNo;
    private String otherCustName;
}