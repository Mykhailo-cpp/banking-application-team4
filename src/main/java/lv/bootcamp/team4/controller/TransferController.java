package lv.bootcamp.team4.controller;

import lv.bootcamp.team4.dto.request.TransferRequest;
import lv.bootcamp.team4.dto.response.TransferResponse;
import lv.bootcamp.team4.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.transfer(request));
    }
}
