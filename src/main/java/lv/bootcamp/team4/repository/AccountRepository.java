package lv.bootcamp.team4.repository;

import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstraction over account and transaction storage.
 * <p>
 * Keeping this behind an interface lets the service layer stay unaware of how
 * data is stored. The current implementation is in-memory; a database-backed
 * implementation could be substituted without touching {@code AccountService}.
 */
public interface AccountRepository {

    /**
     * Persists an account, keyed by its id.
     *
     * @param account the account to store
     * @return the stored account
     */
    Account save(Account account);

    /**
     * Finds an account by id.
     *
     * @param id the account id
     * @return the account, or an empty optional if none exists
     */
    Optional<Account> findById(UUID id);

    /**
     * Returns all stored accounts.
     *
     * @return every account currently held
     */
    List<Account> findAll();

    /**
     * Removes an account and its transaction history.
     * No-op if no account exists with the given id.
     *
     * @param id the account id
     */
    void deleteById(UUID id);

    /**
     * Files a transaction against the given account's history.
     *
     * @param accountId   the account the transaction belongs to
     * @param transaction the transaction to record
     */
    void saveTransaction(UUID accountId, Transaction transaction);

    /**
     * Returns the transaction history for an account, oldest first.
     *
     * @param accountId the account id
     * @return the account's transactions, or an empty list if none
     */
    List<Transaction> findTransactionsByAccountId(UUID accountId);
}