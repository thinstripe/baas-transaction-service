package com.ow.banking.service.model.transaction;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderClassName = "Builder")
public class TransactionsResponse {
    private String hasMore;
    private Integer totalRecords;
    private List<Transaction> transactions;
}