package com.ow.banking.cbs.owbank.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Builder(builderClassName = "Builder")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OwbSuccess {
    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("transferId")
    private String transferId;

    @JsonProperty("balance")
    private String balance;

    @JsonProperty
    private String timestamp;

    @JsonProperty
    private BigDecimal amount;
}