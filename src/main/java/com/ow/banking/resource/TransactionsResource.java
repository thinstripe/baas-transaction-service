package com.ow.banking.resource;

import com.ow.banking.bank.model.transaction.TransferRequest;
import com.ow.banking.bank.model.transaction.TransferResponse;
import com.ow.banking.entity.User;
import com.ow.banking.resource.in.DepositRequest;
import com.ow.banking.resource.in.IntraBankTransferRequest;
import com.ow.banking.resource.out.DepositResponse;
import com.ow.banking.service.PaymentService;
import com.ow.banking.service.TransactionsService;
import com.ow.banking.service.model.transaction.TransactionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class TransactionsResource {
    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/transaction")
    public ResponseEntity<TransactionsResponse> getTransactions(
            @RequestHeader(value = "Authorization", required = true) final String authorization,
            @RequestParam(required = false) final Optional<String> orderBy,
            @RequestParam(required = false) final Optional<String> rowSize,
            @RequestParam(required = false) final Optional<String> page,
            @RequestParam final Optional<String> accountNumber,
            @RequestParam final Optional<String> period,
            @RequestParam final Optional<String> startDate,
            @RequestParam final Optional<String> endDate) {
        log.info("/transaction");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.toString());
        final TransactionsResponse transactionsResponse = transactionsService.getTransactions(
                user,
                orderBy,
                rowSize.map(Integer::valueOf),
                page.map(Integer::valueOf),
                accountNumber,
                period,
                startDate.map(LocalDate::parse),
                endDate.map(LocalDate::parse));
        log.info(transactionsResponse.toString());
        return ResponseEntity.ok(transactionsResponse);
    }

    static boolean isDelayed(final TransferRequest request) {
        return request.getTransferDate() != null;
    }

    static boolean isRecurringDateRange(final TransferRequest request) {
        return request.getTransferStartDate() != null &&
                request.getTransferPeriod() != null &&
                request.getTransferEndDate() != null;
    }

    static boolean isRecurringNumberRange(final TransferRequest request) {
        return request.getTransferStartDate() != null &&
                request.getTransferPeriod() != null &&
                request.getTransferPayments() != null;
    }

    private static IntraBankTransferRequest toIntraBankTransferRequest(final TransferRequest request) {
        return IntraBankTransferRequest.builder()
                .payerAccountNo(request.getFromAccountNumber())
                .payeeAccountNo(request.getToAccountNumber())
                .remark(request.getDescription())
                .transactionAmount(request.getAmount())
                .build();
    }

    @PostMapping(value = "/transaction")
    public ResponseEntity<TransferResponse> transaction(
            @RequestBody final TransferRequest request,
            @RequestHeader(value = "Authorization", required = true) final String authorization) {
        log.info("/transaction");
        log.info(request.toString());
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.toString());
        if (isDelayed(request)) {
            //TODO:
            return ResponseEntity.of(Optional.empty());
        } else if (isRecurringDateRange(request)) {
            //TODO:
            return ResponseEntity.of(Optional.empty());
        } else if (isRecurringNumberRange(request)) {
            //TODO:
            return ResponseEntity.of(Optional.empty());
        } else {
            final IntraBankTransferRequest intraBankTransferRequest = toIntraBankTransferRequest(request);
            return ResponseEntity.ok(paymentService.intrabankTransfer(intraBankTransferRequest, user));
        }
    }

    @PostMapping(value = "/deposit", consumes = "application/json")
    public ResponseEntity<DepositResponse> addDeposit(
            @RequestBody final DepositRequest request,
            @RequestHeader(value = "Authorization", required = true) final String authorization) {
        log.info("/deposit");
        log.info(request.toString());
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.toString());
        final DepositResponse response = paymentService.deposit(request, user);
        log.info(response.toString());
        return ResponseEntity.ok(response);
    }
}