package com.dhiraj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dhiraj.dto.ForgetPasswordLoader;
import com.dhiraj.dto.LoginLoader;
import com.dhiraj.dto.UserRegistrationLoader;
import com.dhiraj.exception.OTPException;
import com.dhiraj.exception.UnexpectedException;
import com.dhiraj.exception.UserRegistrationOrLoginExcption;
import com.dhiraj.mapper.UserRegistrationMapper;
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
	
	@Autowired
	UserRegistrationMapper userRegMap;

	public List<Role> getRole() {
		return roleRepo.findAll();
	}
	
	public UserRegistrationLoader saveData(UserRegistrationLoader userRegLoad) {
		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		if (userRegRepo.findByEmail(userReg.getEmail()) == null) {
			if (userRegRepo.findByContact(userReg.getContact()) == null) {
				userReg.setOtp(otp.generateOTP(6, userReg.getEmail()));
				userReg.setStatus(0);
				userReg.setPassword(passEncDec.encrypt(userReg.getPassword()));
				try {
					String from = sender;
					String to = userReg.getEmail();
					String subject = "Account Varification";
					String content = "Dear " + userReg.getFirstName()+" "+userReg.getLastName()
							+ ", <br> Your account varification for Registration OTP is <br>" + "<h3>"
							+ userReg.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
					sendMail.sendMail(from, to, subject, content);
						return userRegMap.userRegistrationToLoader(userRegRepo.save(userReg));
				} catch (Exception e) {
					throw new UnexpectedException("Some problem is there. Please try again some time otp");
				}
			}
			throw new UserRegistrationOrLoginExcption("Contact Already Exist");
		}
		throw new UserRegistrationOrLoginExcption("Email Already Exist");
	}

	public UserRegistrationLoader varifyAccount(UserRegistrationLoader userRegLoad) {
		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user != null) {
			if(user.getOtp() != null) {
				if (user.getOtp().equals(userReg.getOtp())) {
					user.setStatus(1);
					user.setOtp(null);
					return userRegMap.userRegistrationToLoader(userRegRepo.save(user));
				}
				throw new OTPException("Please Enter Valid OTP");
			}
			throw new OTPException("OTP is Expired. Please regenerate OTP");
		}
		throw new OTPException("User dose not exist");
	}

	public UserRegistrationLoader forgetPassword(UserRegistrationLoader userRegLoad) {
		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		try {
			UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
			if(user!=null) {
				user.setOtp(otp.generateOTP(6, userReg.getEmail()));
				String from = sender;
				String to = userReg.getEmail();
				String subject = "Forget Password";
				String content = "Dear " + user.getFirstName()+" "+user.getLastName() + ", <br> Your forget password OTP is <br>" + "<h3>"
						+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
				sendMail.sendMail(from, to, subject, content);
				return userRegMap.userRegistrationToLoader(userRegRepo.save(user));	
			}
			throw new UserRegistrationOrLoginExcption("Please Check and enter valid Email ID");			
		} catch (Exception e) {
			throw new UnexpectedException("Some problem is there. Please try again some time");
		}
	}

	public UserRegistrationLoader forgetEmail(UserRegistrationLoader userRegLoad) {
		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		UserRegistration user = userRegRepo.findByContact(userReg.getContact());
		if(user!=null) {
			return userRegMap.userRegistrationToLoader(user);
		}
		throw new UserRegistrationOrLoginExcption("Please enter valid Mobile No");
		
	}
	
	public UserRegistrationLoader forgetPasswordSet(ForgetPasswordLoader userReg) {
//		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if (user != null) {
			if(user.getOtp() != null) {
				if (user.getOtp().equals(userReg.getOtp())) {
					user.setPassword(passEncDec.encrypt(userReg.getPassword()));
					user.setStatus(1);
					user.setOtp(null);
					return userRegMap.userRegistrationToLoader(userRegRepo.save(user));
				}
				throw new OTPException("Please Enter Valid OTP");
			}
			throw new OTPException("OTP is Expired. Please regenerate OTP");
		}
		throw new OTPException("User dose not exist");
	}

	public UserRegistrationLoader userLogin(LoginLoader loginLoad) {
		UserRegistration userData;
		userData = userRegRepo.findByEmailOrContactAndPassword(loginLoad.getEmail(), loginLoad.getEmail(), passEncDec.encrypt(loginLoad.getPassword()));
//		if(userData==null)
//		userData = userRegRepo.findByContactAndPassword(loginLoad.getEmail(), passEncDec.encrypt(loginLoad.getPassword()));

		if (userData != null) {
				if(userData.getStatus()==1) {
					return userRegMap.userRegistrationToLoader(userData);
				}
				
				throw new OTPException("please verify your account with OTP That is sended on your Mail ID");
		}
		throw new UserRegistrationOrLoginExcption("Some problem is there please check your username and password");		
	}

	public UserRegistrationLoader resendOTP(UserRegistrationLoader userRegLoad) {
		UserRegistration userReg = userRegMap.loaderToUserRegistration(userRegLoad);
		UserRegistration user = userRegRepo.findByEmail(userReg.getEmail());
		if(user!=null) {
			try {
				user.setOtp(otp.generateOTP(6, userReg.getEmail()));
				String from = sender;
				String to = userReg.getEmail();
				String subject = "Forget Password";
				String content = "Dear " + user.getFirstName()+" "+user.getLastName() + ", <br> Your forget password OTP is <br>" + "<h3>"
						+ user.getOtp() + "</h3>Thank you, <br>Dhiraj Chaudhary, <br>Mail Varification";
				sendMail.sendMail(from, to, subject, content);
				return userRegMap.userRegistrationToLoader(userRegRepo.save(user));
			} catch (Exception e) {
				throw new UnexpectedException("Some problem is there. Please try again some time");
			}
		}
		throw new UserRegistrationOrLoginExcption("Please Enter Valid Email ID");	
	}

}
