package com.ow.banking.service;

import com.ow.banking.bank.BankTransactions;
import com.ow.banking.bank.model.transaction.GetTransactionsRequest;
import com.ow.banking.bank.model.transaction.GetTransactionsResponse;
import com.ow.banking.entity.User;
import com.ow.banking.repository.AccountDetailsRepository;
import com.ow.banking.service.model.transaction.TransactionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.ow.banking.service.TransactionsServiceHelper.toTransactions;

@Slf4j
@Service
public class TransactionsService {
    private static final String DefaultOrderBy = "DESC";
    private static final int DefaultRowSize = 20;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private BankTransactions bankTransactions;

    public TransactionsResponse getTransactions(final User user,
                                                final Optional<String> orderBy,
                                                final Optional<Integer> rowSize,
                                                final Optional<Integer> page,
                                                final Optional<String> accountNumber,
                                                final Optional<String> period,
                                                final Optional<LocalDate> startDate,
                                                final Optional<LocalDate> endDate) {
        final GetTransactionsRequest request = toGetTransactionsRequest(orderBy, rowSize, page, accountNumber, period, startDate, endDate);
        final GetTransactionsResponse response = bankTransactions.getTransactions(request);
        final TransactionsResponse transactionsResponse = TransactionsResponse.builder()
                .transactions(toTransactions(response))
                .build();
        BeanUtils.copyProperties(response, transactionsResponse);
        return transactionsResponse;
    }

    public TransactionsResponse getTransactions(final User user) {
        return getTransactions(user, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    private GetTransactionsRequest toGetTransactionsRequest(final Optional<String> orderBy,
                                                            final Optional<Integer> rowSize,
                                                            final Optional<Integer> page,
                                                            final Optional<String> accountNumber,
                                                            final Optional<String> period,
                                                            final Optional<LocalDate> startDate,
                                                            final Optional<LocalDate> endDate) {
        final GetTransactionsRequest.Builder builder = GetTransactionsRequest.builder()
                .accept("*/*")
                .inquiryOrder("1");
        Optional.of(orderBy.orElse(DefaultOrderBy)).ifPresent(builder::orderBy);
        Optional.of(rowSize.orElse(DefaultRowSize)).ifPresent(builder::rowSize);
        page.map(TransactionsServiceHelper::getNextStartIndex).ifPresent(builder::nextStartIndex);
        accountNumber.flatMap(accountDetailsRepository::findById).ifPresent(ad -> builder
                .custAccountNo(ad.getCustomerAccNumber())
                .currency(ad.getCurrency()));
        period.ifPresent(builder::period);
        startDate.ifPresent(builder::startDate);
        endDate.ifPresent(builder::endDate);
        return builder.build();
    }
}