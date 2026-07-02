package lv.bootcamp.team4.model;

/**
 * Defines the categories of financial operations
 * that can be executed within the banking application.
 */
public enum TransactionType {
    /**
     * An operation in which funds are added to an account,
     * increasing its total balance.
     */
    DEPOSIT,

    /**
     * An operation in which funds are withdrawn from an account,
     * decreasing its total balance.
     */
    WITHDRAWAL,

    /**
     * An operation in which funds are moved directly from one account
     * to another.
     */
    TRANSFER
}
