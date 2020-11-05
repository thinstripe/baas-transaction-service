package com.ow.banking.cbs.owbank;

import com.ow.banking.bank.BankTransfer;
import com.ow.banking.bank.model.transaction.InternalTransferRequest;
import com.ow.banking.bank.model.transaction.TransferResponse;
import com.ow.banking.cbs.owbank.config.OwBankApiConfiguration;
import com.ow.banking.cbs.owbank.transactions.OwbSuccess;
import com.ow.banking.cbs.owbank.transactions.OwbTransferRequest;
import com.ow.banking.cbs.owbank.transactions.OwbTransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Slf4j
public class OwBankTransfer extends OwBankApiConfiguration implements BankTransfer {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public TransferResponse transfer(final InternalTransferRequest request) {
        final OwbTransferRequest owbRequest = toTransferRequest(request);
        final OwbTransferResponse transferResponse = restTemplate
                .postForEntity(this.getTransferUrl(), owbRequest, OwbTransferResponse.class).getBody();
        final String transferId = Optional.ofNullable(transferResponse.getSuccess())
                .map(OwbSuccess::getTransferId)
                .orElse(null);
        return toTransferResponse(request, transferId);
    }

    private static TransferResponse toTransferResponse(final InternalTransferRequest request, final String transferId) {
        return TransferResponse.builder()
                .custAccountNo(request.getPayerAccountNo())
                .custName(request.getPayerAccountName())
                .payeeCustAccountNo(request.getPayeeAccountNo())
                .payeeCustName(request.getPayeeAccountName())
                .txnAmount(Math.round(request.getTxnAmount()))
                .txnDate((new Date()).toString())
                .newHoldId(transferId)
                .build();
    }

    private static OwbTransferRequest toTransferRequest(final InternalTransferRequest request) {
        return OwbTransferRequest.builder()
                .fromAccountId(request.getPayerAccountNo())
                .fromClientId(request.getPayerClientId())
                .toAccountId(request.getPayeeAccountNo())
                .toClientId(request.getPayeeClientId())
                .amount(request.getTxnAmount())
                .description(request.getRemark())
                .build();
    }
}