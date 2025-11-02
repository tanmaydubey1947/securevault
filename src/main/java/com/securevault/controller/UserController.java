package com.securevault.controller;

import com.securevault.model.dto.BaseResponse;
import com.securevault.model.dto.user.UserRequest;
import com.securevault.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired private UserService userService;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("register")
    public ResponseEntity<BaseResponse> register(@RequestBody final UserRequest request) {
        log.info("Initiating User Registration...");
        final BaseResponse response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch user details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User details fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("getUserDetails/{email}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> getUserDetails(@PathVariable final String email) {
        log.info("Initiating fetching user details...");
        final BaseResponse response = userService.getUserDetails(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Verify User Account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User verification completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("verifyUser")
    public ResponseEntity<BaseResponse> verifyUser(@RequestParam final String token) {
        log.info("Initiating user verification...");
        final BaseResponse response = userService.verifyUser(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Generate Password Reset Token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset token generated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("generateResetToken")
    public ResponseEntity<BaseResponse> generateResetToken(@RequestBody final UserRequest request) {
        log.info("Initiating password reset token generation...");
        final BaseResponse response = userService.generateResetToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Reset User Credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset User Credentials Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("resetCredentials")
    public ResponseEntity<BaseResponse> resetCredentials(@RequestBody final UserRequest request) {
        log.info("Initiating credentials reset ...");
        final BaseResponse response = userService.resetCredentials(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
