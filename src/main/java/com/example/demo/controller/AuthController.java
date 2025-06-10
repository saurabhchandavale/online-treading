package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtProvider;
import com.example.demo.modal.TwoFactorOTP;
import com.example.demo.modal.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.AuthResponse;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.TwoFactorOtpService;
import com.example.demo.utils.OtpUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	public UserRepository userRepository;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	public TwoFactorOtpService twoFactorOtpService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{
		System.out.println("received");
		
		
		User isExists = userRepository.findByEmail(user.getEmail());
		if(isExists != null) {
			throw new Exception("Email is already exists");
		}
		User newUser = new User();
		newUser.setFullName(user.getFullName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		System.out.println("SCE : username " + user.getFullName());
		User savedUser = userRepository.save(newUser);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwt = JwtProvider.generateToken(auth);
		
		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setStatus(true);
		response.setMessage("Successfully Registered...");
		
		return new ResponseEntity<AuthResponse>(response, HttpStatus.CREATED);
		
		
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
		System.out.println("received");
		String username = user.getEmail();
		String password = user.getPassword();
		
		Authentication auth = authenticate(username,password);		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwt = JwtProvider.generateToken(auth);
		
		User authUser = userRepository.findByEmail(username);
		
		if(user.getTwoFactorAuth().isEnabled()) {
			AuthResponse response = new AuthResponse();
			response.setMessage("Two factor auth is enabled ");
			response.setTwoFactorAuthEnabled(true);
			
			String otp = OtpUtils.generateOtp();
			TwoFactorOTP oldOtp = twoFactorOtpService.findByUser(authUser.getId());
			
			if(oldOtp != null) {
				twoFactorOtpService.deleteTwoFactorOtp(oldOtp);
			}
			
			TwoFactorOTP newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);
			response.setSession(new TwoFactorOTP().getId());
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		
		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setStatus(true);
		response.setMessage("login Successfully...");
		
		return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
		
		
	}


	private Authentication authenticate(String username, String password) {
		UserDetails userByUsername = customUserDetailsService.loadUserByUsername(username);
		
		if(userByUsername == null) throw new BadCredentialsException("Invalid username ...");
		
		if(!password.equals(userByUsername.getPassword())) throw new BadCredentialsException("Invalid Password ...");
		
		return new UsernamePasswordAuthenticationToken(userByUsername,password, userByUsername.getAuthorities());
	}

}
