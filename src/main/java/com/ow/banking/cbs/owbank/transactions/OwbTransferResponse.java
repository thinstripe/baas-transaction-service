package com.ow.banking.cbs.owbank.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderClassName = "Builder")
@ToString
public class OwbTransferResponse {
    @JsonProperty("success")
    private OwbSuccess success;
}