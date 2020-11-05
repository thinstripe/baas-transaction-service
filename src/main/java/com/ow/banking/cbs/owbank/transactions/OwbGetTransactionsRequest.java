package com.ow.banking.cbs.owbank.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OwbGetTransactionsRequest {
    @JsonProperty("accountId")
    private String accountId;
}