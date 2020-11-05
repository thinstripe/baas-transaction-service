package com.ow.banking.bank;

import com.ow.banking.bank.model.transaction.GetTransactionsRequest;
import com.ow.banking.bank.model.transaction.GetTransactionsResponse;

public interface BankTransactions {
  GetTransactionsResponse getTransactions(GetTransactionsRequest getTransactionsRequest);
}
