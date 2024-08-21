package com.example.spring_boot_demo.config;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.spring_boot_demo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.ignoringRequestMatchers("/api/users/createUser"))
		.authorizeHttpRequests(requests -> requests
					.requestMatchers("/api/users/createUser").permitAll()
				.requestMatchers("/api/users/**").authenticated()
				.anyRequest().permitAll()
				)
			.formLogin(withDefaults())
			.httpBasic(withDefaults());
		return http.build();
	}
	
//  @Bean
//  public UserDetailsService userDetailsService()
//  {
//  	var user = User.builder()
//  			.username("user")
//  			.password(passwordEncoder().encode("password"))
//  			.roles("USER")
//  			.build();
//  	
//  	return new InMemoryUserDetailsManager(user);
//  }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    	return daoAuthenticationProvider;
    }
  @Bean
  public PasswordEncoder passwordEncoder(){
  	return new BCryptPasswordEncoder();
  }
}
