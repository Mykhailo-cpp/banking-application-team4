package lv.bootcamp.team4.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


// The account info we send back to the user.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    // the account id
    private UUID id;

    // the account's iban
    private String iban;

    // name of the owner
    private String ownerName;

    // how much money is in the account
    private BigDecimal balance;
}
