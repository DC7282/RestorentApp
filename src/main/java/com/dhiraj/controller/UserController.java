package com.dhiraj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dhiraj.model.Role;
import com.dhiraj.model.UserRegistration;
import com.dhiraj.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {
//Dhiraj
	@Autowired
	private UserService userServ;

	private final HttpServletRequest request;
	HttpSession session;

	public UserController(HttpServletRequest httpServletRequest, HttpSession session) {
		this.request = httpServletRequest;
		this.session = session;
	}

	@PostMapping("/registerUser")
	public ResponseEntity<?> userRegister(@RequestBody UserRegistration userReg) {
		try {
			boolean user = userServ.saveData(userReg);
			if (user)
				return new ResponseEntity<>("User Register Successfully", HttpStatus.OK);
			else
				return new ResponseEntity<>("Some problem is there", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/verifyAccount")
	public ResponseEntity<?> varifyAccount(@RequestBody UserRegistration userReg) {
		try {
			UserRegistration verify = userServ.varifyAccount(userReg);
			if (verify!=null)
				return new ResponseEntity<>("Account varify using otp Successfully", HttpStatus.OK);
			return new ResponseEntity<>("Please enter valid OTP", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/login")
	public ResponseEntity<?> userLogin(@RequestBody UserRegistration userReg) {

		try {
			UserRegistration userData = userServ.userLogin(userReg);
			if (userData != null) {
					if(userData.getStatus()==1) {
					Role role = userData.getRole();
					request.getSession().setAttribute("uid", userData.getId());
					request.getSession().setAttribute("urole", role.getRole());
					return new ResponseEntity<>(userData, HttpStatus.OK);
				}
					return new ResponseEntity<>("please verify your account with OTP That is sended on your Mail ID",
							HttpStatus.LOCKED);
			}
			return new ResponseEntity<>("Some problem is there please check your username and password",
					HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/forgetPassword")
	public ResponseEntity<?> forgetPassword(@RequestBody UserRegistration userReg) {
		try {
			UserRegistration forget = userServ.forgetPassword(userReg);
			if (forget!=null)
				return new ResponseEntity<>("OTP send succesfully on Email", HttpStatus.OK);
			return new ResponseEntity<>("Please enter correct email ID", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/resendOtp")
	public ResponseEntity<?> resendOTP(@RequestBody UserRegistration userReg) {
		try {
			boolean forget = userServ.resendOTP(userReg);
			if (forget)
				return new ResponseEntity<>("OTP send succesfully on Email", HttpStatus.OK);
			return new ResponseEntity<>("Some problem is there", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/forgetPasswordSet")
	public ResponseEntity<?> forgetPasswordSet(@RequestBody UserRegistration userReg) {
		try {
			boolean verify = userServ.forgetPasswordSet(userReg);
			if (verify)
				return new ResponseEntity<>("Account varify using otp Successfully", HttpStatus.OK);
			return new ResponseEntity<>("Some problem is there", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/destroy-user-auth")
	public ResponseEntity<?> destroySession(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ResponseEntity<>("Logout user Successfully", HttpStatus.OK);
	}
}
