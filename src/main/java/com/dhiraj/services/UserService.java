package com.dhiraj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dhiraj.model.Role;
import com.dhiraj.model.UserRegistration;
import com.dhiraj.repository.RoleRepository;
import com.dhiraj.repository.UserRegistrationRepository;
import com.dhiraj.resources.GenerateOTP;
import com.dhiraj.resources.PasswordEncryptAndDecrypt;
import com.dhiraj.resources.SendMail;

@Service("userServ")
public class UserService {

	@Autowired
	private UserRegistrationRepository userRegRepo;
	
	@Autowired
	private RoleRepository roleRepo;

	@Value("${spring.mail.username}")
	private String sender;
	
	@Autowired
	private GenerateOTP otp;
	
	@Autowired
	private SendMail sendMail;
	
	@Autowired
	private PasswordEncryptAndDecrypt passEncDec;

	public List<Role> getRole() {
		return roleRepo.findAll();
	}
	
	public String saveData(UserRegistration userReg) {
		if (userRegRepo.findByEmail(userReg.getEmail()) == null) {
			if (userRegRepo.findByContact(userReg.getContact()) == null) {
				userReg.setOtp(otp.generateOTP(6, userReg.getEmail()));
				userReg.setStatus(0);
				userReg.setPassword(passEncDec.encrypt(userReg.getPassword()));
				try {
					String from = sender;
					String to = userReg.getEmail();
					String subject = "Account Varification";
					String content = "Dear " + userReg.getName()
							+ ", <br> Your account varification for Registration OTP is <br>" + "<h3>"
							+ userReg.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
					sendMail.sendMail(from, to, subject, content);
					userRegRepo.save(userReg);
					return "Success";
				} catch (Exception e) {
					return "Some problem is there. Please try again some time";
				}
			}
			return "Contact Already Exist";
		}
		return "Email Already Exist";
	}

	public String varifyAccount(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user != null) {
			if(user.getOtp() != null) {
				if (user.getOtp().equals(userReg.getOtp())) {
					user.setStatus(1);
					user.setOtp(null);
					userRegRepo.save(user);
					return "Success";
				}
				return "Please Enter Valid OTP";
			}
			return "OTP is Expired. Please regenerate OTP";
		}
		return "User dose not exist";
	}

	public UserRegistration forgetPassword(UserRegistration userReg) {
		try {
			UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
			user.setOtp(otp.generateOTP(6, userReg.getEmail()));
			String from = sender;
			String to = userReg.getEmail();
			String subject = "Forget Password";
			String content = "Dear " + user.getName() + ", <br> Your forget password OTP is <br>" + "<h3>"
					+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
			sendMail.sendMail(from, to, subject, content);
			return userRegRepo.save(user);
		} catch (Exception e) {
			return null;
		}
	}

	public UserRegistration forgetEmail(UserRegistration userReg) {
		return userRegRepo.findByContact(userReg.getContact());
	}
	
	public String forgetPasswordSet(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user != null) {
			if(user.getOtp() != null) {
				if (user.getOtp().equals(userReg.getOtp())) {
					user.setPassword(passEncDec.encrypt(userReg.getPassword()));
					user.setStatus(1);
					user.setOtp(null);
					userRegRepo.save(user);
					return "Success";
				}
				return "Please Enter Valid OTP";
			}
			return "OTP is Expired. Please regenerate OTP";
		}
		return "User dose not exist";
	}

	public UserRegistration userLogin(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmailAndPassword(userReg.getEmail(), passEncDec.encrypt(userReg.getPassword()));
		if(user!=null)
			user.setPassword(passEncDec.decrypt(user.getPassword())); 
		return user;
	}

	public String resendOTP(UserRegistration userReg) {
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if(user!=null) {
			try {
				user.setOtp(otp.generateOTP(6, userReg.getEmail()));
				String from = sender;
				String to = userReg.getEmail();
				String subject = "Forget Password";
				String content = "Dear " + user.getName() + ", <br> Your forget password OTP is <br>" + "<h3>"
						+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
				sendMail.sendMail(from, to, subject, content);
				userRegRepo.save(user);
				return "Success";
			} catch (Exception e) {
				return "Some problem is there. Please try again some time";
			}
		}
		return "Please Enter Valid Email ID";	
	}

}
