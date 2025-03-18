package com.example.spring_boot_demo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.spring_boot_demo.model.User;
import com.example.spring_boot_demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CustomUserDetailsService implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Fetching user from db: {} ",username);
		
		User user= userRepository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with username: "+username);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
	}

}