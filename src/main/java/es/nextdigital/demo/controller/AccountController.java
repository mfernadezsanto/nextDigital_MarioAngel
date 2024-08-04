package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.Account;
import es.nextdigital.demo.model.Transaction;
import es.nextdigital.demo.service.AccountService;
import es.nextdigital.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(path = "/api/accounts/{accountNumber}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transaction> getTransactions(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return transactionService.getTransactionsByAccountId(account.getId());
    }


}
