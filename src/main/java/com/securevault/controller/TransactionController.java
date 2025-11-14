package com.securevault.controller;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.transaction.TransactionRequest;
import com.securevault.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("sendToWallet")
    public ResponseEntity<BaseResponse> sendToBank(@RequestBody final TransactionRequest request) {
        log.info("Initiating send to bank...");
        final BaseResponse response = transactionService.sendToBank(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
