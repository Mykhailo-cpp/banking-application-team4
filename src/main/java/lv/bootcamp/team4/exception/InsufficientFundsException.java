package lv.bootcamp.team4.exception;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID accountId) {
        super("Insufficient funds in account with id: " + accountId);
    }
}
