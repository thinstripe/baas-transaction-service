package com.ow.banking.resource.in;

import lombok.*;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DepositRequest {
  private String customerAccNumber;
  private Float amount;
  private String remark;
}