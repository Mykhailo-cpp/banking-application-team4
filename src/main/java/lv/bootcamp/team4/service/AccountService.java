package lv.bootcamp.team4.service;

import lv.bootcamp.team4.dto.request.CreateAccountRequest;
import lv.bootcamp.team4.dto.response.AccountResponse;
import lv.bootcamp.team4.dto.response.TransactionResponse;
import lv.bootcamp.team4.exception.AccountNotFoundException;
import lv.bootcamp.team4.exception.InsufficientFundsException;
import lv.bootcamp.team4.mapper.AccountMapper;
import lv.bootcamp.team4.mapper.TransactionMapper;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import lv.bootcamp.team4.repository.AccountRepository;
import lv.bootcamp.team4.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    private final Map<UUID, Object> accountLocks = new ConcurrentHashMap<>();

    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .iban(generateIban())
                .ownerName(request.getOwnerName())
                .balance(request.getInitialBalance())
                .build();

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public AccountResponse getAccount(UUID id) {
        return accountMapper.toResponse(findAccount(id));
    }

    public List<TransactionResponse> getTransactions(UUID id) {
        findAccount(id); // 404 if the account doesn't exist
        return transactionMapper.toResponseList(transactionRepository.findByAccountId(id));
    }

    public Account findAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public void adjustBalance(UUID id, BigDecimal delta) {
        Object lock = accountLocks.computeIfAbsent(id, k -> new Object());
        synchronized (lock) {
            Account account = findAccount(id);
            BigDecimal newBalance = account.getBalance().add(delta);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(id);
            }
            account.setBalance(newBalance);
            accountRepository.save(account);
        }
    }

    public Transaction recordTransaction(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        if (transaction.getAccount() == null || transaction.getAccount().getId() == null) {
            throw new IllegalArgumentException("Transaction must reference a valid account.");
        }
        return transactionRepository.save(transaction);
    }

    private String generateIban() {
        while (true) {
            long number = (long) (Math.random() * 1_000_000_000_000_000L);
            String candidate = String.format("LT%018d", number);
            boolean exists = accountRepository.findAll().stream()
                    .anyMatch(a -> a.getIban().equals(candidate));
            if (!exists) {
                return candidate;
            }
        }
    }
}