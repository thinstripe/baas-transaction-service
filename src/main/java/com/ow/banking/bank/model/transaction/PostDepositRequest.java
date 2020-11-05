package com.ow.banking.bank.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDepositRequest {
  @JsonProperty("bankName")
  public String bankName;

  @JsonProperty("custAccountNo")
  public String custAccountNo;

  @JsonProperty("custAccountName")
  public String custAccountName;

  @JsonProperty("txnCurrency")
  public String txnCurrency;

  @JsonProperty("txnAmount")
  public float txnAmount;

  @JsonProperty("valueDate")
  public String valueDate;

  @JsonProperty("remark")
  public String remark;
}
