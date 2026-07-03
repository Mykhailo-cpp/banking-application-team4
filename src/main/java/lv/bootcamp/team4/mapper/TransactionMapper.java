package lv.bootcamp.team4.mapper;

import lv.bootcamp.team4.dto.response.TransactionResponse;
import lv.bootcamp.team4.dto.response.TransferResponse;
import lv.bootcamp.team4.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction t) {
        UUID accountId = (t.getAccount() != null) ? t.getAccount().getId() : null;
        return TransactionResponse.builder()
                .id(t.getId())
                .accountId(accountId)
                .type(t.getType())
                .amount(t.getAmount())
                .createdAt(t.getCreatedAt())
                .note(t.getNote())
                .build();
    }

    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        return transactions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TransferResponse toTransferResponse(UUID fromAccountId, UUID toAccountId,
                                               BigDecimal amount, String note, LocalDateTime createdAt) {
        return TransferResponse.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .amount(amount)
                .note(note)
                .createdAt(createdAt)
                .build();
    }
}
