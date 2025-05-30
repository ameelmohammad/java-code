package com.firstproject.connection.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.firstproject.connection.Entity.LoginRequest;
import com.firstproject.connection.Entity.User;
import com.firstproject.connection.Service.UserService;

import jakarta.validation.Valid;

@RestController

public class UserController {
	
	@Autowired
	private UserService userService;
	
	 @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
	        if (userService.emailExists(user.getEmail())) {
	            return ResponseEntity.badRequest().body("Email already registered.");
	        }
	        if (userService.mobileExists(user.getMobile())) {
	            return ResponseEntity.badRequest().body("Mobile number already registered.");
	        }
			User savedUser = userService.registerUser(user);
			
	        return ResponseEntity.ok(savedUser);
	    }

	 @PostMapping("/login")
	 public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//		 System.out.println("Identifier received: " + request.getIdentifier());
//		    System.out.println("Password received: " + request.getPassword());
	     return userService.findByIdentifier(request.getIdentifier()) 
	             .filter(u -> u.getPassword().equals(request.getPassword()))
	             .map(u -> {
	                 userService.logLoginAttempt(request.getIdentifier(), request.getPassword());  // <-- Save the login
	                 return ResponseEntity.ok("Login successful!");
	             })
	             .orElse(ResponseEntity.status(401).body("Invalid credentials"));
	     }
	 }
	


	

