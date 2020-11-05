package com.ow.banking.cbs.owbank.config;

import com.ow.banking.cbs.owbank.security.OwBankAuthConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OwBankApiConfiguration {

    @Value("${cbs.owBank.secured.enabled}")
    private Boolean secured;

    @Value("${cbs.owBank.secured.url}")
    private String securedUrl;

    @Value("${cbs.owBank.secured.clientId}")
    private String securedClientId;

    @Value("${cbs.owBank.secured.secret}")
    private String securedSecret;

    @Value("${cbs.owBank.host}")
    private String host;

    @Value("${cbs.owBank.baseUrl.register}")
    private String registerUrl;

    @Value("${cbs.owBank.baseUrl.accounts}")
    private String accountsUrl;

    @Value("${cbs.owBank.baseUrl.deposit}")
    private String depositUrl;

    @Value("${cbs.owBank.baseUrl.activate}")
    private String activateUrl;

    @Value("${cbs.owBank.baseUrl.transactions}")
    private String transactionsUrl;

    @Value("${cbs.owBank.baseUrl.transfer}")
    private String transferUrl;

    @Autowired
    private OwBankAuthConnector owBankAuthConnector;

    public HttpHeaders headers() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("accept", "application/json");

        if (secured) headers.set("Authorization", getAuthToken());

        return headers;

    }

    private String getAuthToken(){
        return "Bearer gr1n&b3@r1t";// TODO: Implement this +getAccessToken();
    }

    public String getSecuredUrl() {
        return this.host.concat(securedUrl);
    }

    public String getSecuredClientId() {
        return this.host.concat(securedClientId);
    }

    public String getSecuredSecret() {
        return this.host.concat(securedSecret);
    }

    public String getBaseUrl() {
        return this.host.concat(host);
    }

    public String getRegisterUrl() {
        return this.host.concat(registerUrl);
    }

    public String getActivateUrl() {
        return this.host.concat(activateUrl);
    }

    public String getAccountsUrl() {
        return this.host.concat(accountsUrl);
    }

    public String getDepositUrl() {
        return this.host.concat(depositUrl);
    }

    public String getTransactionsUrl() {
        return this.host.concat(transactionsUrl);
    }

    public String getTransferUrl() {
        return this.host.concat(transferUrl);
    }
}
