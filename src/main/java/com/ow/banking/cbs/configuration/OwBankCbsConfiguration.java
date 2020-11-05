package com.ow.banking.cbs.configuration;

import com.ow.banking.bank.BankDeposit;
import com.ow.banking.bank.BankTransactions;
import com.ow.banking.bank.BankTransfer;
import com.ow.banking.cbs.owbank.OwBankDeposit;
import com.ow.banking.cbs.owbank.OwBankTransactions;
import com.ow.banking.cbs.owbank.OwBankTransfer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name="cbs.provider", havingValue="ow")
@Configuration
public class OwBankCbsConfiguration {
    @Bean
    public BankTransactions bankTransactions() {
        return new OwBankTransactions();
    }

    @Bean
    public BankDeposit bankDeposit() {
        return new OwBankDeposit();
    }

    @Bean
    public BankTransfer bankTransfer() {
        return new OwBankTransfer();
    }
}