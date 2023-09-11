package com.tictactoebackend.projectapi.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;
import com.tictactoebackend.projectapi.userRepository.userRepository;

@Service
@Transactional
public class userServiceImpl implements userService{

    @Autowired
    userRepository UserRepository;

    @Override
    public user validateUser(String email, String password) throws EtAuthException {
        if(email != null) email = email.toLowerCase();
        return UserRepository.findByEmailandPassword(email, password);
    }

    @Override
    public user registerUser(String username, String email, String password, String role) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email != null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches())
            throw new EtAuthException("Invalid email format");


        Integer count = UserRepository.getCountByEmail(email);
        if(count > 0)
            throw new EtAuthException("email exists");

        String userId = UserRepository.create(username, email, password, role);
        return UserRepository.findById(userId);
    }

}
