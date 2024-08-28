package com.example.spring_boot_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_demo.model.AuthenticationRequest;
import com.example.spring_boot_demo.model.AuthenticationResponse;
import com.example.spring_boot_demo.model.User;
import com.example.spring_boot_demo.service.CustomUserDetailsService;
import com.example.spring_boot_demo.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	 @PostMapping("/authenticate")
	    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		 try {
			 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			 final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			 String jwt = jwtUtil.generateToken(userDetails.getUsername());
			 return ResponseEntity.ok(new AuthenticationResponse(jwt, null));
		 }catch(Exception e) {
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					 .body(new AuthenticationResponse(null, "Invalid username or password "+e.getMessage()));
		 }
	    }
}
