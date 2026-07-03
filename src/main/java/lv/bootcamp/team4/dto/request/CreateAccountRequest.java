package lv.bootcamp.team4.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest {

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}
