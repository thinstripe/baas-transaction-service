package com.ow.banking.cbs.owbank.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder(builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OwbGetTransactionsResponse {
  @JsonProperty
  private List<OwbSuccess> success;
}
