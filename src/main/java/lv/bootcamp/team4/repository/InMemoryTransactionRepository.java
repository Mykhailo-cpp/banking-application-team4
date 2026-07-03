package lv.bootcamp.team4.repository;

import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<UUID, List<Transaction>> byAccount = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        UUID accountId = transaction.getAccount().getId();
        byAccount.computeIfAbsent(accountId, k -> new CopyOnWriteArrayList<>())
                .add(clone(transaction));
        return transaction;
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return byAccount.getOrDefault(accountId, List.of()).stream()
                .map(this::clone)
                .collect(Collectors.toList());
    }

    private Transaction clone(Transaction source) {
        return Transaction.builder()
                .id(source.getId())
                .account(source.getAccount() != null ? cloneAccount(source.getAccount()) : null)
                .type(source.getType())
                .amount(source.getAmount())
                .createdAt(source.getCreatedAt())
                .note(source.getNote())
                .build();
    }

    private Account cloneAccount(Account source) {
        return Account.builder()
                .id(source.getId())
                .iban(source.getIban())
                .ownerName(source.getOwnerName())
                .balance(source.getBalance())
                .build();
    }
}