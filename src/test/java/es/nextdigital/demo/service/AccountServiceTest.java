package es.nextdigital.demo.service;

import es.nextdigital.demo.model.Account;
import es.nextdigital.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    public AccountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountByNumber() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(account);

        Account found = accountService.getAccountByNumber("123456");
        assertEquals(1L, found.getId());
    }
}
