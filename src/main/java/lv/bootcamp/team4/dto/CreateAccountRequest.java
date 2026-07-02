package lv.bootcamp.team4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;


// Data we get from the user when they want to create a new account.
// We don't ask for the iban here because the service makes it.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest {

    // name of the account owner, can't be empty
    @NotBlank(message = "Owner name is required")
    private String ownerName;

    // money the account starts with, can't be negative
    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}
