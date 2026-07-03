package lv.bootcamp.team4.service;


import lv.bootcamp.team4.exception.InsufficientFundsException;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import lv.bootcamp.team4.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransferService  {

    private final AccountService accountService;
    private final AtomicLong transactionIdGenerator = new AtomicLong(1);

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String note) {
        // Get both accounts
        Account fromAccount = accountService.findAccount(fromAccountId);
        Account toAccount = accountService.findAccount(toAccountId);

        // Check sufficient funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccountId);
        }

        // Update balances
        accountService.adjustBalance(fromAccountId, fromAccount.getBalance().subtract(amount));
        accountService.adjustBalance(toAccountId, toAccount.getBalance().add(amount));

        // Create transaction records
        LocalDateTime now = LocalDateTime.now();

        accountService.recordTransaction(new Transaction(
                fromAccountId, fromAccount,
                TransactionType.TRANSFER,
                amount,
                now,
                note != null ? note : "Transfer to account " + toAccountId
        ));

        accountService.recordTransaction(new Transaction(
                toAccountId,toAccount,
                TransactionType.DEPOSIT,
                amount,
                now,
                note != null ? note : "Transfer from account " + fromAccountId
        ));
    }

    public void credit(UUID accountId, BigDecimal amount, String note) {
        Account account = accountService.findAccount(accountId);

        // Add money to account
        accountService.adjustBalance(accountId, account.getBalance().add(amount));

        // Record transaction
        accountService.recordTransaction(new Transaction(
                accountId,
                account,
                TransactionType.DEPOSIT,
                amount,
                LocalDateTime.now(),
                note != null ? note : "Deposit"
        ));
    }

    public void debit(UUID accountId, BigDecimal amount, String note) {
        Account account = accountService.findAccount(accountId);

        // Check sufficient funds
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId);
        }

        // Subtract money from account
        accountService.adjustBalance(accountId, account.getBalance().subtract(amount));

        // Record transaction
        accountService.recordTransaction(new Transaction(
                accountId,account,
                TransactionType.WITHDRAWAL,
                amount,
                LocalDateTime.now(),
                note != null ? note : "Withdrawal"
        ));
    }

    public BigDecimal checkBalance(UUID  accountId) {
        Account account = accountService.findAccount(accountId);
        return account.getBalance();
    }
}

