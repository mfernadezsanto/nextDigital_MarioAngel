package es.nextdigital.demo.service;

import es.nextdigital.demo.model.Transaction;
import es.nextdigital.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

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
}
