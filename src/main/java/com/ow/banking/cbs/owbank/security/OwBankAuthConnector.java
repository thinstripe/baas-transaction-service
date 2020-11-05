package com.ow.banking.cbs.owbank.security;

import com.ow.banking.cbs.owbank.config.OwBankApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OwBankAuthConnector extends OwBankApiConfiguration {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${stubbank.baseUrl}")
  private String securityUrl;

}
