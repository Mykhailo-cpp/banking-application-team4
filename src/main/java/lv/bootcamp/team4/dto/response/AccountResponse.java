package lv.bootcamp.team4.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private UUID id;
    private String iban;
    private String ownerName;
    private BigDecimal balance;
}
