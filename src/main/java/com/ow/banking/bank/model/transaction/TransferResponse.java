package com.ow.banking.bank.model.transaction;

import lombok.*;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TransferResponse {
    private String cardNumber;
    private String currency;
    private String custAccountNo;
    private String custAccountType;
    private String custName;
    private Integer holdAmount;
    private Integer inAccountBalance;
    private Integer inAmount;
    private String inSuspendBillNumber;
    private String newHoldId;
    private Integer outAccountBalance;
    private String outSuspendBillNumber;
    private String payeeAccountProduct;
    private String payeeCardNo;
    private String payeeCurrency;
    private String payeeCustAccountNo;
    private String payeeCustAccountType;
    private String payeeCustName;
    private String productId;
    private Integer txnAmount;
    private String txnDate;
}