package lv.bootcamp.team4.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    @NotNull(message = "Source account id is required")
    private UUID fromAccountId;

    @NotNull(message = "Target account id is required")
    private UUID toAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String note;
}
