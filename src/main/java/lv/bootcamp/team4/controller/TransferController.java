package lv.bootcamp.team4.controller;

import jakarta.validation.Valid;
import lv.bootcamp.team4.dto.TransferRequest;
import lv.bootcamp.team4.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<String> createTransfer(
            @Valid @RequestBody TransferRequest request) {

        transferService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                request.getNote()
        );

        return ResponseEntity.ok("Transfer successful");
    }
}