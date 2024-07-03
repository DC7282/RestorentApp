package com.dhiraj.resources;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateOTP {

	@Autowired
	private OTPCleanup cleanup;
	
	public String generateOTP(int length, String email) {
		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}

		cleanup.cleanup(email);
		return new String(otp);
	}
		
}
