package lv.bootcamp.team4.service;

import lv.bootcamp.team4.dto.AccountResponse;
import lv.bootcamp.team4.dto.CreateAccountRequest;
import lv.bootcamp.team4.dto.TransactionResponse;
import lv.bootcamp.team4.exception.AccountNotFoundException;
import lv.bootcamp.team4.exception.InsufficientFundsException;
import lv.bootcamp.team4.mapper.AccountMapper;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import lv.bootcamp.team4.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Account-level business logic: creating accounts, looking them up, listing
 * their transaction history, and the balance/history mutations that
 * {@code TransferService} relies on.
 * <p>
 * Holds no storage of its own (delegated to {@link AccountRepository}) and no
 * DTO mapping of its own (delegated to {@link AccountMapper}), so its single
 * responsibility is account business logic.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    /**
     * Per-account lock objects. Balance updates for the same account are
     * serialized on a dedicated private lock, so a read-modify-write can't lose
     * an update, while updates to different accounts still run concurrently.
     * A private lock object is used rather than the account id itself to avoid
     * sharing a lock with any unrelated code.
     */
    private final Map<UUID, Object> accountLocks = new ConcurrentHashMap<>();

    /**
     * @param accountRepository storage abstraction for accounts and transactions
     * @param accountMapper     maps domain models to response DTOs
     */
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * Creates a new account from the supplied request and stores it.
     *
     * @param request the validated account-creation request
     * @return a response view of the newly created account
     */
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .iban(request.getIban())
                .ownerName(request.getOwnerName())
                .balance(request.getInitialBalance())
                .build();

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    /**
     * Returns a response view of the account with the given id.
     *
     * @param id the account id
     * @return the account as a response DTO
     * @throws AccountNotFoundException if no account exists with that id
     */
    public AccountResponse getAccount(UUID id) {
        return accountMapper.toResponse(findAccount(id));
    }

    /**
     * Lists the transaction history for the given account, oldest first.
     *
     * @param id the account id
     * @return the account's transactions as response DTOs
     * @throws AccountNotFoundException if no account exists with that id
     */
    public List<TransactionResponse> getTransactions(UUID id) {
        findAccount(id); // ensure the account exists before returning history
        return accountRepository.findTransactionsByAccountId(id).stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes the account with the given id, along with its transaction history.
     *
     * @param id the account id
     * @throws AccountNotFoundException if no account exists with that id
     */
    public void deleteAccount(UUID id) {
        findAccount(id); // fail fast with 404 if the account doesn't exist
        accountRepository.deleteById(id);
        accountLocks.remove(id); // drop the per-account lock so the map doesn't grow unbounded
    }

    /**
     * Looks up an account by id.
     *
     * @param id the account id
     * @return the stored account (a detached copy)
     * @throws AccountNotFoundException if no account exists with that id
     */
    public Account findAccount(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Applies a signed change to an account's balance and persists it.
     * A negative delta debits, a positive delta credits. The update is
     * serialized per account to prevent lost updates, and rejected if it would
     * drive the balance below zero.
     *
     * @param id    the account id
     * @param delta the amount to add (may be negative)
     * @throws AccountNotFoundException   if no account exists with that id
     * @throws InsufficientFundsException if the resulting balance would be negative
     */
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

    /**
     * Records a transaction against the history of the account it references.
     * The transaction's id is assigned here if not already set.
     *
     * @param transaction the transaction to store
     * @return the stored transaction
     * @throws IllegalArgumentException if the transaction has no referenced account
     */
    public Transaction recordTransaction(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }

        Account referenced = transaction.getAccount();
        if (referenced == null || referenced.getId() == null) {
            throw new IllegalArgumentException("Transaction must reference a valid account.");
        }

        accountRepository.saveTransaction(referenced.getId(), transaction);
        return transaction;
    }
}