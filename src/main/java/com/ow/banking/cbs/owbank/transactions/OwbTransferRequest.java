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
public class OwbTransferRequest {
    @JsonProperty("fromClientId")
    public String fromClientId;

    @JsonProperty("fromAccountId")
    public String fromAccountId;

    @JsonProperty("toClientId")
    public String toClientId;

    @JsonProperty("toAccountId")
    public String toAccountId;

    @JsonProperty("amount")
    public float amount;

    @JsonProperty("description")
    public String description;
}