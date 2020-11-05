package com.ow.banking;

import com.ow.banking.bank.BankDeposit;
import com.ow.banking.bank.model.transaction.PostDepositRequest;
import com.ow.banking.bank.model.transaction.PostDepositResponse;
import com.ow.banking.entity.User;
import com.ow.banking.repository.AccountDetailsRepository;
import com.ow.banking.resource.in.DepositRequest;
import com.ow.banking.resource.out.DepositResponse;
import com.ow.banking.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaymentServiceTest {
    static final String customerAccNumber1 = "93494593934";
    static final Float accountBalance1 = 10.25f;
    static final Float depositAmount1 = 100.95f;

    @TestConfiguration
    static class PaymentServiceTestConfiguration {
        @Bean
        BankDeposit bankDeposit() {
            return new BankDeposit() {
                @Override
                public PostDepositResponse deposit(final PostDepositRequest request) {
                    return PostDepositResponse.builder().inAccountBalance(accountBalance1 + depositAmount1).build();
                }
            };
        }
    }

    @Autowired
    private TestDataFactory dataFactory;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private PaymentService paymentService;

    @Before
    public void setup() {
        dataFactory.clear();
    }

    @Test
    public void testDeposit() {
        // Given
        final User user = dataFactory.createUser();
        final String userId = user.getUserId();
        dataFactory.createAccountDetails(userId, customerAccNumber1);

        // When
        final DepositRequest request = DepositRequest.builder()
                .customerAccNumber(customerAccNumber1)
                .amount(depositAmount1)
                .remark("Initial deposit")
                .build();
        final DepositResponse response = paymentService.deposit(request, user);

        // Then
        assertEquals(accountBalance1 + depositAmount1, response.getCurrentBalance(), 0f);
    }
}