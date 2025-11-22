package com.securevault.controller;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.model.dto.transaction.TransactionResponse;
import com.securevault.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Send to wallet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User transaction successful"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("sendToWallet")
    public ResponseEntity<BaseResponse> sendToWallet(@RequestBody final TransactionRequest request) {
        log.info("Initiating send to wallet...");
        final BaseResponse response = transactionService.sendToWallet(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Send to bank")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User transaction successful"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("sendToBank")
    public ResponseEntity<BaseResponse> sendToBank(@RequestBody final TransactionRequest request) {
        log.info("Initiating send to bank...");
        final BaseResponse response = transactionService.sendToBank(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Add to bank")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User transaction successful"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("addToWallet")
    public ResponseEntity<BaseResponse> addToWallet(@RequestBody final TransactionRequest request) {
        log.info("Initiating add money to wallet...");
        final BaseResponse response = transactionService.sendToBank(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Fetch all transactions - Admin only")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All transactions fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @GetMapping("getAllTransactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() { //TODO: Implement Pagination and Filtering
        log.info("Fetching all transaction details...");
        final List<TransactionResponse> response = transactionService.getAllTransactions();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Adjustment to/from system wallet - Admin only")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Adjustment successful"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("adjustment")
    public ResponseEntity<BaseResponse> adjustment(@RequestBody final TransactionRequest request) {
        log.info("Initiating adjustment...");
        final BaseResponse response = transactionService.adjustment(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
