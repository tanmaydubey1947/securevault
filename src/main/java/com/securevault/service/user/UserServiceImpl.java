package com.securevault.service.user;


import com.securevault.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired private UserDao userDao;



}
