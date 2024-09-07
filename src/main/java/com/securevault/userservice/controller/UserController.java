package com.securevault.userservice.controller;


import com.securevault.userservice.dto.AuthRequest;
import com.securevault.userservice.entity.AddMoney;
import com.securevault.userservice.entity.SendMoney;
import com.securevault.userservice.entity.UserInfo;
import com.securevault.userservice.service.JwtService;
import com.securevault.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getUsername()), HttpStatus.ACCEPTED);
        } else {
            throw new UsernameNotFoundException("Authentication Failure...");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getUserInfoById(@PathVariable int id) {
        return new ResponseEntity<>(userService.getUserInfoById(id), HttpStatus.ACCEPTED);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserInfo(@RequestBody UserInfo userInfo) {
        return new ResponseEntity<>(userService.addUser(userInfo), HttpStatus.CREATED);
    }

    @PostMapping("/addMoney")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> addMoney(@RequestBody AddMoney addMoney) {
        return new ResponseEntity<>(userService.addMoney(addMoney), HttpStatus.OK);
    }

    @PostMapping("/sendMoney")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> sendMoney(@RequestBody SendMoney sendMoney) {
        return new ResponseEntity<>(userService.sendMoney(sendMoney), HttpStatus.OK);
    }

}
