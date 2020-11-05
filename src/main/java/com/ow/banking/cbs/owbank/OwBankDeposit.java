package com.ow.banking.cbs.owbank;

import com.ow.banking.bank.BankDeposit;
import com.ow.banking.bank.model.transaction.PostDepositRequest;
import com.ow.banking.bank.model.transaction.PostDepositResponse;
import com.ow.banking.cbs.owbank.config.OwBankApiConfiguration;
import com.ow.banking.cbs.owbank.transactions.OwbPostDepositRequest;
import com.ow.banking.cbs.owbank.transactions.OwbPostDepositResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class OwBankDeposit extends OwBankApiConfiguration implements BankDeposit {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PostDepositResponse deposit(PostDepositRequest postDepositRequest) {
        final OwbPostDepositRequest request = OwbPostDepositRequest.builder()
                .accountId(postDepositRequest.getCustAccountNo())
                .amount(postDepositRequest.getTxnAmount())
                .build();
        final ResponseEntity<OwbPostDepositResponse> responseEntity = restTemplate
                .postForEntity(this.getDepositUrl(), request, OwbPostDepositResponse.class);
        //TODO: get balance or update conductor
        final Float currentBalance = Float.parseFloat(responseEntity.getBody().getSuccess().getTransactionId()) * -1;
        return PostDepositResponse.builder().inAccountBalance(currentBalance).build();
    }
}