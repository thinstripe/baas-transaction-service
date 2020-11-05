package com.ow.banking.resource.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class IntraBankTransferRequest {
  @JsonProperty
  private String payerAccountNo;

  @JsonProperty
  private String payeeAccountNo;

  @JsonProperty
  private float transactionAmount;

  @JsonProperty
  private String remark;
}