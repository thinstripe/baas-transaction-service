package com.ow.banking.cbs.owbank.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderClassName = "Builder")
public class OwbPostDepositResponse {
    @JsonProperty("success")
    private OwbSuccess success;
}