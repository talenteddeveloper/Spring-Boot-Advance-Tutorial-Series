package com.example.spring_boot_demo.config;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.spring_boot_demo.filter.JwtRequestFilter;
import com.example.spring_boot_demo.service.CustomUserDetailsService;

// Udemy Course: https://www.udemy.com/share/10c2y5/
// Youtube playlist: https://www.youtube.com/playlist?list=PLyzY2l387AlMe2DLp_aWHIbhSiDmHr5xw
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	//Learn user roles : https://youtu.be/nN68jjUP_rQ
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeHttpRequests((requests) -> requests
       		 .requestMatchers("/api/authenticate").permitAll()
            .requestMatchers("/api/users/**").hasRole("USER")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
		  .sessionManagement(session -> session
	        		 .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	         http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//	         .formLogin(withDefaults())
//	         .httpBasic(withDefaults());
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
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
