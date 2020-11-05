package com.ow.banking.bank.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PostDepositResponse {
  @JsonProperty("inAccountBalance")
  private Float inAccountBalance;
}