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
    /**
     * The unique internal identifier for this specific transaction record.
     */
    private String id;

    /**
     * The identifier of the account from which funds are being deducted.
     * Note: This may be null or empty for a standard DEPOSIT operation.
     */
    private String sourceAccountId;

    /**
     * The identifier of the account receiving the funds.
     * Note: This may be null or empty for a standard WITHDRAWAL operation.
     */
    private String destinationAccountId;

    /**
     * The amount of money involved in the transaction.
     */
    private BigDecimal amount;

    /**
     * The categorization of this operation (DEPOSIT, WITHDRAWAL, or TRANSFER).
     */
    private TransactionType type;

    /**
     * The date and time when the transaction was executed and recorded
     * in the system.
     */
    private LocalDateTime timestamp;
}
