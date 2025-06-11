package com.example.demo.service;

import com.example.demo.domain.VerificationType;
import com.example.demo.modal.User;
import com.example.demo.modal.VerificationCode;

public interface VerificationCodeService {
	
	VerificationCode sendVerificationCode(User user, VerificationType verificationType);
	VerificationCode getVerificationCodeById(Long id) throws Exception;
	VerificationCode getVerificationCodeByUser(Long userId);
	void deleteVerificationCodeById(VerificationCode verificationCode);

}
