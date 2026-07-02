package lv.bootcamp.team4.mapper;

import lv.bootcamp.team4.dto.AccountResponse;
import lv.bootcamp.team4.dto.TransactionResponse;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Translates domain models into their API response DTOs.
 * <p>
 * Extracted from the service layer because more than one service needs to
 * produce transaction responses: {@code AccountService} when listing history
 * and {@code TransferService} after executing a transfer. Centralising the
 * mapping here keeps those services from depending on one another purely for
 * formatting, and gives response shaping a single place to change.
 */
@Component
public class AccountMapper {

    /**
     * Maps an {@link Account} to its response view.
     *
     * @param account the domain account
     * @return the account response DTO
     */
    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .build();
    }

    /**
     * Maps a {@link Transaction} to its response view.
     * The referenced account is flattened to its id so the account's live
     * balance is never embedded in transaction output.
     *
     * @param transaction the domain transaction
     * @return the transaction response DTO
     */
    public TransactionResponse toResponse(Transaction transaction) {
        UUID accountId = (transaction.getAccount() != null)
                ? transaction.getAccount().getId()
                : null;

        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountId(accountId)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .note(transaction.getNote())
                .build();
    }
}