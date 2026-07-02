package lv.bootcamp.team4.model;

import lombok.*;

import java.util.UUID;

/**
 * Represents a user entity within the application.
 * This class holds the core identity and authorization information
 * for users with the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    /**
     * The unique internal identifier for the user (e.g., a UUID).
     */
    private UUID id;

    /**
     * The unique name chosen by the user for login and display purposes.
     */
    private String username;

    /**
     * Contains the hashed password.
     */
    private String passwordHash;

    /**
     * The authorization level associated with the user.
     */
    private Role role;
}
