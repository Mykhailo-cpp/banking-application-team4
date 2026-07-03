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
        accountService.adjustBalance(accountId, amount);

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

        // Subtract money from account (adjustBalance handles insufficient funds validation)
        accountService.adjustBalance(accountId, amount.negate());

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