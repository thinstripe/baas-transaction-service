package com.ow.banking;

import com.ow.banking.entity.AccountDetails;
import com.ow.banking.entity.User;
import com.ow.banking.repository.AccountDetailsRepository;
import com.ow.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;

public class TestDataFactory {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public void clear() {
        Arrays.asList(accountDetailsRepository, userRepository).forEach(CrudRepository::deleteAll);
    }

    public AccountDetails createAccountDetails(final String userId, final String accountNo) {
        final AccountDetails accountDetails = AccountDetails.builder()
                .userId(userId)
                .cardNumber("93434498494")
                .cbsId("9449393494")
                .currency("MYR")
                .customerAccNumber(accountNo)
                .subAccountSerialNo("394945949")
                .build();
        return accountDetailsRepository.save(accountDetails);
    }

    public User createUser() {
        final User user = User.builder()
                .userId("1")
                .firstName("John")
                .lastName("Smith")
                .email("john.mmith@blah.com")
                .password("password")
                .customerNo("123")
                .build();
        return userRepository.save(user);
    }   
}
