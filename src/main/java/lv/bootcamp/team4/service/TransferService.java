package lv.bootcamp.team4.service;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import lv.bootcamp.team4.model.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TransferService  {

    private final AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String note) {
        // Get both accounts
        Account fromAccount = accountService.findAccount(fromAccountId);
        Account toAccount = accountService.findAccount(toAccountId);

        // Update balances (adjustBalance handles insufficient funds validation)
        accountService.adjustBalance(fromAccountId, amount.negate());
        accountService.adjustBalance(toAccountId, amount);

        // Create transaction records
        LocalDateTime now = LocalDateTime.now();

        accountService.recordTransaction(Transaction.builder()
                .account(fromAccount)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .createdAt(now)
                .note(note != null ? note : "Transfer to account " + toAccountId)
                .build());

        accountService.recordTransaction(Transaction.builder()
                .account(toAccount)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .createdAt(now)
                .note(note != null ? note : "Transfer from account " + fromAccountId)
                .build());
    }

    public void credit(UUID accountId, BigDecimal amount, String note) {
        Account account = accountService.findAccount(accountId);

        // Add money to account
        accountService.adjustBalance(accountId, amount);

        // Record transaction
        accountService.recordTransaction(Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .note(note != null ? note : "Deposit")
                .build());
    }

    public void debit(UUID accountId, BigDecimal amount, String note) {
        Account account = accountService.findAccount(accountId);

        // Subtract money from account (adjustBalance handles insufficient funds validation)
        accountService.adjustBalance(accountId, amount.negate());

        // Record transaction
        accountService.recordTransaction(Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .note(note != null ? note : "Withdrawal")
                .build());
    }

}