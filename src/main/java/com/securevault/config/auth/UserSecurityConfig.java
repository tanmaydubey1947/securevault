package com.securevault.config.auth;

import com.securevault.service.auth.AuthUserDetailsService;
import com.securevault.service.auth.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class UserSecurityConfig {

    @Autowired private JwtAuthFilter jwtAuthFilter;
    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request

                        // ---------------------------
                        // PUBLIC ENDPOINTS
                        // ---------------------------
                        .requestMatchers(
                                "/auth/authenticate",
                                "/auth/refreshToken",
                                "/user/register",
                                "/user/verifyUser",
                                "/user/resetCredentials",
                                "/user/generateResetToken",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/error"
                        ).permitAll()

                        // ---------------------------
                        // ROLE-BASED ENDPOINTS
                        // ---------------------------
                        // ADMIN-only transaction endpoints
                        .requestMatchers(
                                "/transaction/getAllTransactions/**",
                                "/transaction/adjustment"
                        ).hasRole("ADMIN")

                        // USER + ADMIN for all other transaction requests
                        .requestMatchers("/transaction/**")
                        .hasAnyRole("USER", "ADMIN")

                        // USER-only endpoint
                        .requestMatchers("/user/getUserTransactions")
                        .hasRole("USER")

                        // USER or ADMIN
                        .requestMatchers("/user/getUserDetails")
                        .hasAnyRole("USER", "ADMIN")

                        // ---------------------------
                        // AUTHENTICATED FALLBACK RULES
                        // ---------------------------
                        .requestMatchers("/auth/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}