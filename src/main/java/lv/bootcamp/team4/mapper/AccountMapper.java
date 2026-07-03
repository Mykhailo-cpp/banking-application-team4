package lv.bootcamp.team4.mapper;

import lv.bootcamp.team4.dto.response.AccountResponse;
import lv.bootcamp.team4.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .build();
    }
}