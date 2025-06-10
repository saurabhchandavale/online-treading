package com.example.demo.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modal.TwoFactorOTP;
import com.example.demo.modal.User;
import com.example.demo.repository.TwoFactorOtpRepository;
@Service
public class TwoFactorServiceImpl implements TwoFactorOtpService {
	@Autowired
	TwoFactorOtpRepository twoFactorOtpRepository;

	@Override
	public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		TwoFactorOTP twoFactorOtp = new TwoFactorOTP();
		twoFactorOtp.setId(id);
		twoFactorOtp.setJwt(jwt);
		twoFactorOtp.setOtp(otp);
		twoFactorOtp.setUser(user);
		TwoFactorOTP save = twoFactorOtpRepository.save(twoFactorOtp);
		return save;
	}

	@Override
	public TwoFactorOTP findByUser(Long userId) {
		
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP> byId = twoFactorOtpRepository.findById(id);
		return byId.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
		twoFactorOtpRepository.delete(twoFactorOtp);
	}

}

