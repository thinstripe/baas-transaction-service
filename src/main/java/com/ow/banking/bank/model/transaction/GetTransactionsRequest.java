package com.ow.banking.bank.model.transaction;

import lombok.*;

import java.time.LocalDate;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class GetTransactionsRequest {
  private String accept;
  private String orderBy;
  private Integer rowSize;
  private String custAccountNo;
  private String currency;
  private String nextStartIndex;
  private String inquiryOrder;
  private LocalDate startDate;
  private LocalDate endDate;
  private String period;
}
