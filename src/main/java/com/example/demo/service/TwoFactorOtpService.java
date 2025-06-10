package com.example.demo.service;


import com.example.demo.modal.TwoFactorOTP;
import com.example.demo.modal.User;

public interface TwoFactorOtpService {
	
	TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);
	TwoFactorOTP findByUser(Long userId);
	TwoFactorOTP findById(String id);
	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);
	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);

}
