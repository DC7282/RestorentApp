package com.dhiraj.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dhiraj.model.UserRegistration;
import com.dhiraj.repository.UserRegistrationRepository;

import jakarta.mail.internet.MimeMessage;

@Service("userServ")
public class UserService {

	@Autowired
	private UserRegistrationRepository userRegRepo;

	@Autowired
	private JavaMailSender mailSender;

	public static String generateOTP(int length) {
		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return new String(otp);
	}

	@Value("${spring.mail.username}")
	private String sender;

	private void sendMail(String from, String to, String subject, String content) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean saveData(UserRegistration userReg) {
		userReg.setOtp(generateOTP(6));
		userReg.setStatus(0);
		try {
			String from = sender;
			String to = userReg.getEmail();
			String subject = "Account Varification";
			String content = "Dear " + userReg.getName()
					+ ", <br> Your account varification for Registration OTP is <br>" + "<h3>" + userReg.getOtp()
					+ "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";

			sendMail(from, to, subject, content);
			userRegRepo.save(userReg);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean varifyAccount(UserRegistration userReg) {

		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user.getOtp().equals(userReg.getOtp())) {
			user.setStatus(1);
			user.setOtp(null);
			userRegRepo.save(user);
			return true;
		}
		return false;
	}
//	try {
//		UserRegistration user = userRegRepo.findByEmail(email);
//		String from = sender;
//		String to = email;
//		String subject = "Account Varification";
//		String content = "Dear "+user.getName()+", <br> You account varification for Registration OTP is <br>"
//				+ "<h3></h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
//				
//		sendMail(from, to, subject, content);
//	}catch(Exception e) {
//		
//	}

	public boolean forgetPassword(UserRegistration userReg) {
		try {
			UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
			user.setOtp(generateOTP(6));
			String from = sender;
			String to = userReg.getEmail();
			String subject = "Forget Password";
			String content = "Dear " + user.getName() + ", <br> Your forget password OTP is <br>" + "<h3>"
					+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";

			sendMail(from, to, subject, content);
			userRegRepo.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean forgetPasswordSet(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user.getOtp().equals(userReg.getOtp())) {
			user.setPassword(userReg.getPassword());
			user.setOtp(null);
			userRegRepo.save(user);
			return true;
		}
		return false;
	}

	public UserRegistration userLogin(UserRegistration userReg) {
		return userRegRepo.findByEmailAndPassword(userReg.getEmail(), userReg.getPassword());
	}

	public boolean resendOTP(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		try {
			if (user.getOtp() != null) {
				String from = sender;
				String to = userReg.getEmail();
				String subject = "Forget Password";
				String content = "Dear " + user.getName() + ", <br> Your forget password OTP is <br>" + "<h3>"
						+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
				sendMail(from, to, subject, content);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
