package com.ow.banking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CbsApplicationRunner implements ApplicationRunner {

  @Value("${cbs.provider}")
  private String cbsProvider;

  private static final Map<String, String> cbsProviderNames = new HashMap<String, String>() {{
      put("ow", "Oliver Wyman");
      put("tm", "Thoughtmachine");
  }};

  private String getCbsProviderName() {
    return cbsProviderNames.getOrDefault(cbsProvider, "Unknown");
  }

  @Override
  public void run(final ApplicationArguments args) {
    log.info("Core banking provider is '" +  cbsProvider + "' (" +  getCbsProviderName() + ")");
  }
}