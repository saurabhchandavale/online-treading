package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.VerificationType;
import com.example.demo.modal.User;
import com.example.demo.modal.VerificationCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import com.example.demo.service.VerificationCodeService;

import jakarta.mail.MessagingException;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private VerificationCodeService verificationCodeService;

	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
		User user = userService.findUserByJwt(jwt);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt,
			@PathVariable VerificationType verificationType) throws MessagingException {
		User user = userService.findUserByJwt(jwt);
		VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

		if (verificationCode == null) {
			verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
		}

		if (verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendVerificationOtpToEmail(user.getEmail(), verificationCode.getOtp());
		}
		return new ResponseEntity<String>("otp sent successfully", HttpStatus.OK);
	}

	@PatchMapping("/api/users/two-factor-verification/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
			@PathVariable String otp) throws Exception {
		User user = userService.findUserByJwt(jwt);
		VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
		String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
				? verificationCode.getEmail()
				: verificationCode.getMobile();
		
		boolean isVerified = verificationCode.getOtp().equals(otp);
		if(isVerified) {
			User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
			
			verificationCodeService.deleteVerificationCodeById(verificationCode);
			return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
		}
		throw new Exception("Invalid otp...");
	}
}
