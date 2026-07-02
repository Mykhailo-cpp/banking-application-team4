package lv.bootcamp.team4.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


// Data we get from the user when they want to send money between accounts.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    // the account we take the money from
    @NotNull(message = "Source account id is required")
    private UUID fromAccountId;

    // the account we send the money to
    @NotNull(message = "Target account id is required")
    private UUID toAccountId;

    // how much to send, has to be more than 0
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    // optional message about the transfer
    private String note;
}
