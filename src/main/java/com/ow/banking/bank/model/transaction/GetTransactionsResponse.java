package com.ow.banking.bank.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder(builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetTransactionsResponse {
    @JsonProperty
    private String hasMore;
    @JsonProperty
    private Integer totalRecords;
    @JsonProperty
    private List<TxtArray> txtArray;
}
