package com.ow.banking.resource.out;

import lombok.*;

@Builder(builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepositResponse {
  public Float currentBalance;
}