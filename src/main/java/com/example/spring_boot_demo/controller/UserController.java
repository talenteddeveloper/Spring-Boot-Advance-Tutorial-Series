package com.example.spring_boot_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_demo.model.User;
import com.example.spring_boot_demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Udemy Course: https://www.udemy.com/share/10c2y5/
// Youtube playlist: https://www.youtube.com/playlist?list=PLyzY2l387AlMe2DLp_aWHIbhSiDmHr5xw

@RestController
@RequestMapping("/api/users")
public class UserController {

//	@GetMapping
//	public String getUsers() {
//		return "Hello API";
//	}
	private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    
	@Autowired
	private UserRepository userRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping("/createUser")
	public User createUser(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		logger.info("Getting userdetails for id: "+id);
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());

		return userRepository.save(user);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

		userRepository.delete(user);

		return ResponseEntity.ok().build();
	}
}
