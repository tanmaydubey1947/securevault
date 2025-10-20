package com.securevault.service.auth;

import com.securevault.dao.UserDao;
import com.securevault.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired private UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(dao.getUserByEmail(email));
        return user.map(AuthUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found in system"));
    }
}