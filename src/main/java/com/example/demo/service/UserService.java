package com.example.demo.service;

import com.example.demo.domain.VerificationType;
import com.example.demo.modal.User;

public interface UserService {
	
	public User findUserByJwt(String jwt);
	public User finduserByEmail(String email);
	public User findUserById(Long id);
	public User enableTwoFactorAuthentication(VerificationType type, String sentTo, User user);
	public User updatePassword(User user, String newPassword);
	

}
