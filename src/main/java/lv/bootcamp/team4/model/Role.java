package lv.bootcamp.team4.model;

/**
 * Defines the authorization roles available within the application,
 * determining the actions that a user can perform therein.
 */
public enum Role {
    /**
     * A standard system user.
     * Restricted to accessing and managing their own personal data,
     * accounts, and transactions.
     */
    USER,

    /**
     * An administrator with elevated system privileges.
     * Has unrestricted access to view, manage, and modify all user
     * accounts and transactions.
     */
    ADMIN
}
