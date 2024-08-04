package es.nextdigital.demo.service;

import es.nextdigital.demo.model.Account;
import es.nextdigital.demo.model.Card;
import es.nextdigital.demo.model.Transaction;
import es.nextdigital.demo.repository.AccountRepository;
import es.nextdigital.demo.repository.CardRepository;
import es.nextdigital.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    public TransactionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionsByAccountId() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(100.0);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(200.0);

        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getTransactionsByAccountId(1L);
        assertEquals(2, transactions.size());
        assertEquals(100.0, transactions.get(0).getAmount());
        assertEquals(200.0, transactions.get(1).getAmount());
    }

    @Test
    void testWithdrawDebitCard() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("1234567890");
        account.setAccountType("Savings");
        account.setBalance(1000.0);

        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("1111222233334444");
        card.setCardType("Debit");
        card.setAccount(account);

        when(cardRepository.findByCardNumber("1111222233334444")).thenReturn(card);

        Transaction transaction = transactionService.withdraw("1111222233334444", 100.0, false);

        assertNotNull(transaction);
        assertEquals(900.0, account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testWithdrawCreditCard() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("1234567890");
        account.setAccountType("Savings");

        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("1111222233334444");
        card.setCardType("Credit");
        card.setCreditLimit(1000.0);
        card.setCurrentCredit(200.0);
        card.setAccount(account);

        when(cardRepository.findByCardNumber("1111222233334444")).thenReturn(card);

        Transaction transaction = transactionService.withdraw("1111222233334444", 100.0, false);

        assertNotNull(transaction);
        assertEquals(300.0, card.getCurrentCredit());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testDeposit() {
        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("1234567890");
        account.setAccountType("Savings");
        account.setBalance(1000.0);

        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("1111222233334444");
        card.setCardType("Debit");
        card.setAccount(account);

        when(cardRepository.findByCardNumber("1111222233334444")).thenReturn(card);

        Transaction transaction = transactionService.deposit("1111222233334444", 100.0, true);

        assertNotNull(transaction);
        assertEquals(1100.0, account.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }
}
