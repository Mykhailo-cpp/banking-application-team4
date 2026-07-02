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
    private String id;

    /**
     * The IBAN for this account.
     */
    private String iban;

    /**
     * The full name of the account owner, ready for display purposes.
     */
    private String ownerName;

    /**
     * The current balance.
     * Using BigDecimal ensures financial precision
     * and prevents floating-point rounding errors.
     */
    private BigDecimal balance;
}
