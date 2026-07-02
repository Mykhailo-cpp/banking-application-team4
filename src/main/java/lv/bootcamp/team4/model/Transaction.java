package lv.bootcamp.team4.model;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;
import java.math.BigDecimal;


/**
 * Represents a financial transaction record within the system.
 * This class tracks the movement of funds
 * between accounts and other operations.
 */
@Data
@Builder
public class Transaction {
    private String id;

    /**
     * Direct reference to the Account object.
     */
    private Account account;

    private TransactionType type;

    private BigDecimal amount;

    /**
     * The date and time when the transaction was executed.
     */
    private LocalDateTime createdAt;

    /**
     * The description or reason for the transaction.
     */
    private String note;
}
