package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modal.VerificationCode;
@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{

	public VerificationCode findByUserId(Long id);
}
