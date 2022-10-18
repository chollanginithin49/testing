package com.task.controllers;

import java.util.Optional;

import javax.servlet.http.*;

import com.task.entities.User;
import com.task.services.AuthenticationService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = { "http://localhost:8080" },allowCredentials = "true")
@RestController
public class SignInController {
	
    static final int EIGHT_DAYS=8 * 24 * 60 * 60;
    @Autowired
    private AuthenticationService authentication;
    @PostMapping("/sign-in")
    public Optional<User> signIn (@RequestBody String userCredentials,HttpServletResponse response){
        JSONObject obj = new JSONObject(userCredentials);
		String email = obj.getString("Email");
		String password = obj.getString("Password");
        Optional<User> user = authentication.authenticate(email, password);
        if( !user.isPresent()){
            return Optional.empty();
        }
        Cookie cookie = new Cookie("id",Long.toString(user.get().getId()));
        cookie.setMaxAge(EIGHT_DAYS);
        cookie.setSecure(true);
        response.addCookie(cookie);
        return user;
        
    }
}

