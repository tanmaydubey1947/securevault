package com.securevault.userservice.service;

import com.securevault.userservice.entity.AddMoney;
import com.securevault.userservice.entity.SendMoney;
import com.securevault.userservice.entity.UserInfo;
import com.securevault.userservice.exception.InvalidUserInformation;
import com.securevault.userservice.producer.ProducerService;
import com.securevault.userservice.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProducerService producerService;

    public UserInfo getUserInfoById(int id) {
        return userInfoRepository.findById(id).get();
    }

    public UserInfo addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return userInfoRepository.save(userInfo);
    }

    public String addMoney(AddMoney addMoney) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(addMoney.getUsername())) {
            throw new InvalidUserInformation("Invalid User");
        }
        return producerService.addMoney(addMoney);
    }

    public String sendMoney(SendMoney sendMoney) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(sendMoney.getUsername())) {
            throw new InvalidUserInformation("Invalid User");
        }
        return producerService.sendMoney(sendMoney);
    }
}
