package com.example.spring_boot_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_demo.model.User;
import com.example.spring_boot_demo.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
//	@GetMapping
//	public String getUsers() {
//		return "Hello API";
//	}

	@GetMapping
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
