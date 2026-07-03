package lv.bootcamp.team4.repository;

import lv.bootcamp.team4.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        Account copy = clone(account);
        accounts.put(copy.getId(), copy);
        return clone(copy);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id)).map(this::clone);
    }

    @Override
    public List<Account> findAll() {
        return accounts.values().stream().map(this::clone).collect(Collectors.toList());
    }

    private Account clone(Account source) {
        return Account.builder()
                .id(source.getId())
                .iban(source.getIban())
                .ownerName(source.getOwnerName())
                .balance(source.getBalance())
                .build();
    }
}