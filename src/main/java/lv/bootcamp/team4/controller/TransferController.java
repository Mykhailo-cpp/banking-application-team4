package lv.bootcamp.team4.controller;


import jakarta.validation.Valid;
import lv.bootcamp.team4.dto.MoneyRequest;
import lv.bootcamp.team4.dto.TransferRequest;
import lv.bootcamp.team4.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    // POST /transaction/transfer - Transfer money between accounts
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@Valid @RequestBody TransferRequest request) {
        transferService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                request.getNote()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer successful");
        response.put("fromAccountId", request.getFromAccountId());
        response.put("toAccountId", request.getToAccountId());
        response.put("amount", request.getAmount());

        return ResponseEntity.ok(response);
    }

    // POST /transaction/{accountId}/credit - Deposit money
    @PostMapping("/{accountId}/credit")
    public ResponseEntity<Map<String, Object>> credit(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyRequest request) {

        transferService.credit(accountId, request.getAmount(), request.getNote());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Credit successful");
        response.put("accountId", accountId);
        response.put("amount", request.getAmount());
        response.put("newBalance", transferService.checkBalance(accountId));

        return ResponseEntity.ok(response);
    }

    // POST /transaction/{accountId}/debit - Withdraw money
    @PostMapping("/{accountId}/debit")
    public ResponseEntity<Map<String, Object>> debit(
            @PathVariable UUID accountId,
            @Valid @RequestBody MoneyRequest request) {

        transferService.debit(accountId, request.getAmount(), request.getNote());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Debit successful");
        response.put("accountId", accountId);
        response.put("amount", request.getAmount());
        response.put("newBalance", transferService.checkBalance(accountId));

        return ResponseEntity.ok(response);
    }

    // GET /transaction/{accountId}/balance - Check balance
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Map<String, Object>> checkBalance(@PathVariable UUID accountId) {
        BigDecimal balance = transferService.checkBalance(accountId);

        Map<String, Object> response = new HashMap<>();
        response.put("accountId", accountId);
        response.put("balance", balance);

        return ResponseEntity.ok(response);
    }
}
