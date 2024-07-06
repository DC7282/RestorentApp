package com.dhiraj.controller;

import java.util.List;

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

	@Autowired
	private UserService userServ;

	private final HttpServletRequest request;
	HttpSession session;

	public UserController(HttpServletRequest httpServletRequest, HttpSession session) {
		this.request = httpServletRequest;
		this.session = session;
	}
	
	@GetMapping("/viewRoles")
	public List<Role> getRole(){
		return userServ.getRole();
	}

	@PostMapping("/registerUser")
	public ResponseEntity<?> userRegister(@RequestBody UserRegistration userReg) {
		try {
			String result = userServ.saveData(userReg);
			if (result.equalsIgnoreCase("Success"))
				return new ResponseEntity<>("User Register Successfully", HttpStatus.OK);
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/verifyAccount")
	public ResponseEntity<?> varifyAccount(@RequestBody UserRegistration userReg) {
		try {
			String result = userServ.varifyAccount(userReg);
			if (result.equalsIgnoreCase("Success"))
				return new ResponseEntity<>("Account varify using otp Successfully", HttpStatus.OK);
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<>("Please enter valid Email ID", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/forgetEmail")
	public ResponseEntity<?> forgetEmail(@RequestBody UserRegistration userReg) {
		try {
			UserRegistration forget = userServ.forgetEmail(userReg);
			if (forget!=null)
				return new ResponseEntity<>(forget.getEmail(), HttpStatus.OK);
			return new ResponseEntity<>("Please enter valid Mobile No", HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/resendOtp")
	public ResponseEntity<?> resendOTP(@RequestBody UserRegistration userReg) {
		try {
			String result = userServ.resendOTP(userReg);
			if (result.equalsIgnoreCase("Success"))
				return new ResponseEntity<>("OTP send succesfully on Email", HttpStatus.OK);
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/forgetPasswordSet")
	public ResponseEntity<?> forgetPasswordSet(@RequestBody UserRegistration userReg) {
		try {
			String result = userServ.forgetPasswordSet(userReg);
			if (result.equalsIgnoreCase("Success"))
				return new ResponseEntity<>("Account varify using otp Successfully", HttpStatus.OK);
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
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
