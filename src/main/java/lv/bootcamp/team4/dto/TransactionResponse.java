package lv.bootcamp.team4.dto;

import lombok.*;
import lv.bootcamp.team4.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


// The transaction info we send back to the user.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    // the transaction id
    private UUID id;

    // what kind of transaction it is (deposit, withdrawal or transfer)
    private TransactionType type;

    // how much money the transaction was for
    private BigDecimal amount;

    // when it happened
    private LocalDateTime createdAt;

    // optional message about the transaction
    private String note;
}
