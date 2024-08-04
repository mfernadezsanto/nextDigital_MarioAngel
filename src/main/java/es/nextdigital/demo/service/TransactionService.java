package es.nextdigital.demo.service;

import es.nextdigital.demo.model.Account;
import es.nextdigital.demo.model.Card;
import es.nextdigital.demo.model.Transaction;
import es.nextdigital.demo.repository.AccountRepository;
import es.nextdigital.demo.repository.CardRepository;
import es.nextdigital.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    public Transaction withdraw(String cardNumber, Double amount, boolean isExternalATM) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card == null) {
            throw new IllegalArgumentException("Card not found");
        }

        Account account = card.getAccount();
        double fee = isExternalATM ? 5.0 : 0.0; // Example fee for external ATM
        double totalAmount = amount + fee;

        if (card.getCardType().equals("Debit")) {
            if (account.getBalance() < totalAmount) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            account.setBalance(account.getBalance() - totalAmount);
        } else if (card.getCardType().equals("Credit")) {
            if (card.getCurrentCredit() + totalAmount > card.getCreditLimit()) {
                throw new IllegalArgumentException("Credit limit exceeded");
            }
            card.setCurrentCredit(card.getCurrentCredit() + totalAmount);
        }

        Transaction transaction = new Transaction();
        transaction.setType("Withdrawal");
        transaction.setAmount(amount);
        transaction.setDescription(isExternalATM ? "External ATM withdrawal" : "ATM withdrawal");
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        if (fee > 0) {
            Transaction feeTransaction = new Transaction();
            feeTransaction.setType("Fee");
            feeTransaction.setAmount(fee);
            feeTransaction.setDescription("ATM fee");
            feeTransaction.setAccount(account);
            transactionRepository.save(feeTransaction);
        }

        accountRepository.save(account);
        cardRepository.save(card);

        return transaction;
    }
}

