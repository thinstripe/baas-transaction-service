package com.ow.banking.cbs.owbank;

import com.ow.banking.bank.BankTransactions;
import com.ow.banking.bank.model.transaction.GetTransactionsRequest;
import com.ow.banking.bank.model.transaction.GetTransactionsResponse;
import com.ow.banking.bank.model.transaction.TxtArray;
import com.ow.banking.cbs.owbank.transactions.OwbGetTransactionsRequest;
import com.ow.banking.cbs.owbank.transactions.OwbGetTransactionsResponse;
import com.ow.banking.cbs.owbank.transactions.OwbSuccess;
import com.ow.banking.cbs.owbank.config.OwBankApiConfiguration;
import com.ow.banking.repository.AccountDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class OwBankTransactions extends OwBankApiConfiguration implements BankTransactions {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Override
    public GetTransactionsResponse getTransactions(final GetTransactionsRequest getTransactionsRequest) {
        final OwbGetTransactionsResponse response = toGetTransactionsResponse(getTransactionsRequest);
        final GetTransactionsResponse.Builder builder = GetTransactionsResponse.builder();
        if (response.getSuccess() == null)
            builder.totalRecords(0).build();
        else {
            final ArrayList<TxtArray> txtArrays = response.getSuccess().stream()
                    .map(account -> toTxtArray(account, getTransactionsRequest))
                    .collect(Collectors.toCollection(ArrayList::new));
            builder.totalRecords(response.getSuccess().size())
                    .txtArray(txtArrays)
                    .build();
        }
        return builder.build();
    }

    private OwbGetTransactionsResponse toGetTransactionsResponse(final GetTransactionsRequest getTransactionsRequest) {
        final OwbGetTransactionsRequest request = OwbGetTransactionsRequest.builder()
                .accountId(getTransactionsRequest.getCustAccountNo())
                .build();
        return restTemplate.postForEntity(this.getTransactionsUrl(), request, OwbGetTransactionsResponse.class).getBody();
    }

    private static TxtArray toTxtArray(final OwbSuccess account, final GetTransactionsRequest request) {
        return TxtArray.builder()
                .custAccountNo(request.getCustAccountNo())
                .txnAmount(account.getAmount().intValue())
                .txnDate(LocalDate.parse(account.getTimestamp())) //TODO: check format
                .build();
    }
}