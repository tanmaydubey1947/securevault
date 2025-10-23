package com.securevault.service.auth;

import com.securevault.model.dto.auth.AuthRequest;
import com.securevault.model.dto.auth.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (authenticate.isAuthenticated()) {
            String token = jwtService.generateToken(request.getEmail());
            return buildAuthResponse(token);
        } else {
            throw new UsernameNotFoundException("Authentication Failure...");
        }
    }

    private AuthResponse buildAuthResponse(String token) {
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpiresIn(jwtExpiration);
        response.setMsg("Authentication Successful");
        return response;
    }

}
