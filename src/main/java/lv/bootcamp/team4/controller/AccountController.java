package lv.bootcamp.team4.controller;

import jakarta.validation.Valid;
import lv.bootcamp.team4.dto.AccountResponse;
import lv.bootcamp.team4.dto.CreateAccountRequest;
import lv.bootcamp.team4.dto.TransactionResponse;
import lv.bootcamp.team4.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID id) {

        AccountResponse response = accountService.getAccount(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @PathVariable UUID id) {

        List<TransactionResponse> response =
                accountService.getTransactions(id);

        return ResponseEntity.ok(response);
    }
}