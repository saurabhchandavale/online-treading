package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.VerificationType;
import com.example.demo.modal.User;
import com.example.demo.modal.VerificationCode;
import com.example.demo.repository.VerificationCodeRepository;
import com.example.demo.utils.OtpUtils;
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
	@Autowired
	private VerificationCodeRepository verificationCodeRepository;

	@Override
	public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(OtpUtils.generateOtp());
		verificationCode.setVerificationType(verificationType);
		verificationCode.setUser(user);
		return verificationCodeRepository.save(verificationCode);
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) throws Exception {
		Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
		
		if(verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new Exception("Verification code not found");
	}

	@Override
	public VerificationCode getVerificationCodeByUser(Long userId) {
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verificationCode) {
		verificationCodeRepository.delete(verificationCode);
	}
	
	

}
