package lv.bootcamp.team4.exception;

import java.util.UUID;


// Thrown when we can't find an account with the given id.
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID id) {
        super("Account not found: " + id);
    }
}
