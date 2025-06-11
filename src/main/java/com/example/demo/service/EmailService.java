package com.example.demo.service;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private JavaMailSender javaMailSender;
	
	public void sendVerificationOtpToEmail(String email, String otp) throws MessagingException {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,"utf8");
		
		String subject = "Verify - OTP";
		String text = "Your verification otp is " + otp;
		
		messageHelper.setSubject(subject);
		messageHelper.setText(text);
		messageHelper.setTo(email);
		
		try {
			javaMailSender.send(mimeMessage);
		}catch (Exception e) {
			throw new MailSendException(e.getMessage());
		}
		
	}
}
