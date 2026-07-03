package lv.bootcamp.team4.repository;

import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe, in-memory implementation of {@link AccountRepository}.
 * <p>
 * Two techniques keep it safe under Spring's multi-threaded request handling:
 * <ul>
 *   <li>{@link ConcurrentHashMap} for the backing stores.</li>
 *   <li>Defensive copying on every read and write, so callers hold detached
 *       copies and can never mutate stored state by reference. This mirrors how
 *       a real database hands back detached rows.</li>
 * </ul>
 * Data lives only for the lifetime of the process and is lost on restart.
 */
@Repository
public class InMemoryAccountRepository implements AccountRepository {

    /** Primary account store, keyed by account id. */
    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    /** Per-account transaction history, keyed by account id. */
    private final Map<UUID, List<Transaction>> transactionsByAccount = new ConcurrentHashMap<>();

    /**
     * Stores a defensive copy of the account and returns a fresh detached copy.
     *
     * @param account the account to store
     * @return a detached copy of the stored account
     */
    @Override
    public Account save(Account account) {
        Account copy = cloneAccount(account);
        accounts.put(copy.getId(), copy);
        transactionsByAccount.computeIfAbsent(copy.getId(), k -> new ArrayList<>());
        return cloneAccount(copy);
    }

    /**
     * Returns a detached copy of the account with the given id, if present.
     *
     * @param id the account id
     * @return an optional detached copy of the account
     */
    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id)).map(this::cloneAccount);
    }

    /**
     * Returns detached copies of all stored accounts.
     *
     * @return a list of detached account copies
     */
    @Override
    public List<Account> findAll() {
        return accounts.values().stream()
                .map(this::cloneAccount)
                .collect(Collectors.toList());
    }

    /**
     * Removes the account and its transaction history from storage.
     * Silently does nothing if the id is not present.
     *
     * @param id the account id
     */
    @Override
    public void deleteById(UUID id) {
        accounts.remove(id);
        transactionsByAccount.remove(id);
    }

    /**
     * Appends a defensive copy of the transaction to the account's history.
     * The {@code compute} block makes the list update atomic per account.
     *
     * @param accountId   the account the transaction belongs to
     * @param transaction the transaction to record
     */
    @Override
    public void saveTransaction(UUID accountId, Transaction transaction) {
        transactionsByAccount.compute(accountId, (id, list) -> {
            List<Transaction> target = (list == null) ? new ArrayList<>() : list;
            target.add(cloneTransaction(transaction));
            return target;
        });
    }

    /**
     * Returns detached copies of an account's transaction history, oldest first.
     * The {@code compute} block reads the list under the same per-account lock
     * that {@link #saveTransaction} uses, so it can't observe a partial write.
     *
     * @param accountId the account id
     * @return a list of detached transaction copies (empty if none)
     */
    @Override
    public List<Transaction> findTransactionsByAccountId(UUID accountId) {
        List<Transaction> snapshot = transactionsByAccount.compute(accountId, (id, list) -> list);
        if (snapshot == null) {
            return List.of();
        }
        return snapshot.stream()
                .map(this::cloneTransaction)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // Defensive-copy helpers
    // ---------------------------------------------------------------------

    /**
     * Returns a detached copy of an account.
     *
     * @param source the account to copy
     * @return a new, independent account instance
     */
    private Account cloneAccount(Account source) {
        return Account.builder()
                .id(source.getId())
                .iban(source.getIban())
                .ownerName(source.getOwnerName())
                .balance(source.getBalance())
                .build();
    }

    /**
     * Returns a detached copy of a transaction, including a detached copy of its
     * referenced account. Copies every field so no data is lost in storage.
     *
     * @param source the transaction to copy
     * @return a new, independent transaction instance
     */
    private Transaction cloneTransaction(Transaction source) {
        return Transaction.builder()
                .id(source.getId())
                .account(source.getAccount() != null ? cloneAccount(source.getAccount()) : null)
                .type(source.getType())
                .amount(source.getAmount())
                .createdAt(source.getCreatedAt())
                .note(source.getNote())
                .build();
    }
}