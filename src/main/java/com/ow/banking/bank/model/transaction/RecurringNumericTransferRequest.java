package com.ow.banking.bank.model.transaction;

import java.time.LocalDate;

public class RecurringNumericTransferRequest {
    LocalDate transferStartDate;
    String transferPeriod;
    Integer transferPayments;
}