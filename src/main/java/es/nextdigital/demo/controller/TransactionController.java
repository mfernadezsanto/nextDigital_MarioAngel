package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.Transaction;
import es.nextdigital.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/api/transactions/withdraw")
    public Transaction withdraw(@RequestParam String cardNumber, @RequestParam Double amount, @RequestParam boolean isExternalATM) {
        return transactionService.withdraw(cardNumber, amount, isExternalATM);
    }

    @PostMapping("/api/transactions/deposit")
    public Transaction deposit(@RequestParam String cardNumber, @RequestParam Double amount, @RequestParam boolean isSameBankATM) {
        return transactionService.deposit(cardNumber, amount, isSameBankATM);
    }
}
