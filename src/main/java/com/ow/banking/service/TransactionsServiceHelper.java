package com.ow.banking.service;

import com.ow.banking.bank.model.transaction.GetTransactionsResponse;
import com.ow.banking.bank.model.transaction.TransferRequest;
import com.ow.banking.bank.model.transaction.TxtArray;
import com.ow.banking.service.model.transaction.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionsServiceHelper {
    private static Transaction toTransaction(final TxtArray txtArray) {
        return Transaction.builder()
                .txnAmount(txtArray.getTxnAmount())
                .txnDate(txtArray.getTxnDate())
                .category(txtArray.getCustAccountType())
                .destinationAccountNumber(txtArray.getCustAccountNo())
                .destinationName(txtArray.getOtherCustName())
                .build();
    }

    static List<Transaction> toTransactions(final GetTransactionsResponse response) {
        return Optional.ofNullable(response.getTxtArray())
                .orElse(Collections.emptyList())
                .stream()
                .map(TransactionsServiceHelper::toTransaction).collect(Collectors.toList());
    }

    static String getNextStartIndex(final Integer pageNumber) {
        return String.valueOf(pageNumber - 1);
    }
}