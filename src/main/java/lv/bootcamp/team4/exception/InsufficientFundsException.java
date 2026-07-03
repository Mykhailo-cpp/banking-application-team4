package lv.bootcamp.team4.exception;

import java.util.UUID;


// Thrown when an account doesn't have enough money for a transfer.
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(UUID id) {
        super("Insufficient funds in account: " + id);
    }
}
