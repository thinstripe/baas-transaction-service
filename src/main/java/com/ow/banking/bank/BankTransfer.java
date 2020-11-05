package com.ow.banking.bank;

import com.ow.banking.bank.model.transaction.InternalTransferRequest;
import com.ow.banking.bank.model.transaction.TransferResponse;

public interface BankTransfer {
  TransferResponse transfer(InternalTransferRequest transferRequest);
}