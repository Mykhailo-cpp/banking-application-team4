package lv.bootcamp.team4.model;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;


/**
 * Represents a bank account entity within the system.
 * This class serves as the core data model for user accounts, tracking
 * ownership and current balances.
 */
@Data
@Builder
public class Account {
    /**
     * The unique internal identifier for this account record (e.g., a UUID).
     */
    private String id;

    /**
     * The unique identifier of the User who owns this account.
     * This acts as a foreign key reference to the User model.
     */
    private String userId;

    /**
     * The public-facing account number (e.g., an IBAN)
     * used for transactions and display purposes.
     */
    private String accountNumber;

    /**
     * The current available funds in this account.
     */
    private BigDecimal balance;
}
