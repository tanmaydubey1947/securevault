package com.securevault.controller;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.auth.AuthRequest;
import com.securevault.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired private AuthService authService;

    @Operation(summary = "Generate Token")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Token generated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("authenticate")
    public ResponseEntity<BaseResponse> authenticate(@RequestBody final AuthRequest request) {
        log.info("Initiating token generation...");
        final BaseResponse response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
