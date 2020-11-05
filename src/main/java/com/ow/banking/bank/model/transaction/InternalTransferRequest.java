package com.ow.banking.bank.model.transaction;

import lombok.*;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class InternalTransferRequest {
    private String payerClientId;
    private String payerAccountNo;
    private String payerAccountName;

    private String payeeAccountName;
    private String payeeAccountNo;
    private String payeeClientId;

    private String remark;
    private float txnAmount;

    private String markTheBill;
    private String suspendedBillNo;
    private String txnCurrency;
    private String valueDate;
}