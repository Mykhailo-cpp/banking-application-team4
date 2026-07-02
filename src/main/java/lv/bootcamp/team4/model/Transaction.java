package lv.bootcamp.team4.model;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    private String id;
    private String sourceAccountId;
    private String destinationAccountId;
    private double amount;
    private TransactionType type;
    private LocalDateTime timestamp;
}
