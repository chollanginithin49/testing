package com.task.services;

import java.util.Optional; 

import com.task.entities.User;
import com.task.repositories.UserRepository;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService {
    private UserRepository userRepository;
    @Autowired
    AuthenticationService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public Optional<User> authenticate(String email,String password){
        String hashedPassword=hashPassword(password);
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isPresent()
            && user.get().getPassword().equals(hashedPassword)
            )
                return user;
        return Optional.empty();
    }
    private String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }
}
