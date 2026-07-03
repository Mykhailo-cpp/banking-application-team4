package lv.bootcamp.team4.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private Account account;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String note;
}
