package com.ow.banking.service;

import com.ow.banking.bank.BankDeposit;
import com.ow.banking.bank.BankTransfer;
import com.ow.banking.bank.model.transaction.PostDepositRequest;
import com.ow.banking.bank.model.transaction.PostDepositResponse;
import com.ow.banking.bank.model.transaction.InternalTransferRequest;
import com.ow.banking.bank.model.transaction.TransferResponse;
import com.ow.banking.entity.AccountDetails;
import com.ow.banking.entity.User;
import com.ow.banking.repository.AccountDetailsRepository;
import com.ow.banking.repository.UserRepository;
import com.ow.banking.resource.in.DepositRequest;
import com.ow.banking.resource.in.IntraBankTransferRequest;
import com.ow.banking.resource.out.DepositResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.ow.banking.utils.DateUtils.getISOStandardDateFromTimeZone;
import static com.ow.banking.validation.Validation.validationException;

@Service
public class PaymentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private BankDeposit bankDeposit;

    @Autowired
    private BankTransfer bankTransfer;

    public DepositResponse deposit(final DepositRequest request, final User user) {
        final String customerAccountNumber = Optional.ofNullable(request.getCustomerAccNumber())
                .orElseThrow(validationException(user, "Customer account number not provided"));
        final AccountDetails accountDetails = accountDetailsRepository.findById(customerAccountNumber)
                .orElseThrow(validationException(user, "Account " + customerAccountNumber + " not found"));
        final Date valueDate = new Date(); //TODO:
        final PostDepositResponse postDepositResponse = bankDeposit.deposit(PostDepositRequest.builder()
                .custAccountName(toAccountName(user))
                .custAccountNo(customerAccountNumber)
                .remark(request.getRemark())
                .txnAmount(request.getAmount())
                .txnCurrency(accountDetails.getCurrency())
                .valueDate(getISOStandardDateFromTimeZone(valueDate))
                .build());
        final Float currentBalance = Optional.ofNullable(postDepositResponse.getInAccountBalance())
                .orElseThrow(validationException(user, "Account balance is null"));
        return DepositResponse.builder().currentBalance(currentBalance).build();
    }

    public TransferResponse intrabankTransfer(final IntraBankTransferRequest request, final User payerUser) {
        final String payerAccountNo = Optional.ofNullable(request.getPayerAccountNo())
                .orElseThrow(validationException(payerUser, "Payer account number is missing"));
        final AccountDetails payerAccountDetails = accountDetailsRepository.findById(payerAccountNo)
                .orElseThrow(validationException(payerUser, "Payer account not found"));
        final String payeeAccountNo = Optional.ofNullable(request.getPayeeAccountNo())
                .orElseThrow(validationException(payerUser, "Payee account number is missing"));
        final AccountDetails payeeAccountDetails = accountDetailsRepository.findById(payeeAccountNo)
                .orElseThrow(validationException(payerUser, "Payee account not found"));
        final User payeeUser = userRepository.findByUserId(payeeAccountDetails.getUserId())
                .orElseThrow(validationException(payerUser, "Payee user not found for id " + payeeAccountDetails.getUserId()));
        final Date valueDate = new Date(); //TODO:
        final InternalTransferRequest transferRequest = InternalTransferRequest.builder()
                .payerAccountName(toAccountName(payerUser))
                .payerAccountNo(payerAccountDetails.getCustomerAccNumber())
                .payerClientId(payerAccountDetails.getCbsId())
                .txnCurrency(payerAccountDetails.getCurrency())
                .txnAmount(request.getTransactionAmount())
                .payeeAccountName(toAccountName(payeeUser))
                .payeeAccountNo(payeeAccountDetails.getCustomerAccNumber())
                .payeeClientId(payeeAccountDetails.getCbsId())
                .valueDate(getISOStandardDateFromTimeZone(valueDate))
                .remark(request.getRemark())
                .build();
        return bankTransfer.transfer(transferRequest);
    }

    static String toAccountName(final User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}