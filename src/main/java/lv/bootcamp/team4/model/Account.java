package lv.bootcamp.team4.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private UUID id;
    private String iban;
    private String ownerName;
    private BigDecimal balance;
}
