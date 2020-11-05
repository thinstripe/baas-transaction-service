package com.ow.banking.bank;

import com.ow.banking.bank.model.transaction.PostDepositRequest;
import com.ow.banking.bank.model.transaction.PostDepositResponse;

public interface BankDeposit {
  PostDepositResponse deposit(PostDepositRequest postDepositRequest);
}
