package lv.bootcamp.team4.service;

import lv.bootcamp.team4.dto.request.TransferRequest;
import lv.bootcamp.team4.dto.response.TransferResponse;
import lv.bootcamp.team4.mapper.TransactionMapper;
import lv.bootcamp.team4.model.Account;
import lv.bootcamp.team4.model.Transaction;
import lv.bootcamp.team4.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountService accountService;
    private final TransactionMapper transactionMapper;

    public TransferResponse transfer(TransferRequest request) {
        UUID fromId = request.getFromAccountId();
        UUID toId = request.getToAccountId();
        BigDecimal amount = request.getAmount();
        String note = request.getNote();

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        Account source = accountService.findAccount(fromId);
        Account destination = accountService.findAccount(toId);

        // Debit source (throws if insufficient), then credit destination.
        accountService.adjustBalance(fromId, amount.negate());
        accountService.adjustBalance(toId, amount);

        LocalDateTime now = LocalDateTime.now();

        accountService.recordTransaction(Transaction.builder()
                .account(source)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .createdAt(now)
                .note(note != null ? note : "Transfer to account " + toId)
                .build());

        accountService.recordTransaction(Transaction.builder()
                .account(destination)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .createdAt(now)
                .note(note != null ? note : "Transfer from account " + fromId)
                .build());

        return transactionMapper.toTransferResponse(fromId, toId, amount, note, now);
    }
}
