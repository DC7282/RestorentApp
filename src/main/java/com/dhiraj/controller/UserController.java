package com.dhiraj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dhiraj.dto.ForgetPasswordLoader;
import com.dhiraj.dto.LoginLoader;
import com.dhiraj.dto.UserRegistrationLoader;
import com.dhiraj.exception.OTPException;
import com.dhiraj.model.Role;
import com.dhiraj.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin
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
	public ResponseEntity<UserRegistrationLoader> userRegister(@Valid @RequestBody UserRegistrationLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.saveData(userReg), HttpStatus.CREATED);
	}

	@PostMapping("/verifyAccount")
	public ResponseEntity<UserRegistrationLoader> varifyAccount(@RequestBody UserRegistrationLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.varifyAccount(userReg), HttpStatus.OK);
	}

	@GetMapping("/login")
	public ResponseEntity<UserRegistrationLoader> userLogin(@RequestBody LoginLoader userReg) {
		UserRegistrationLoader userData = userServ.userLogin(userReg);
		if (userData != null) {
			Role role = userData.getRole();
			request.getSession().setAttribute("uid", userData.getId());
			request.getSession().setAttribute("urole", role.getRole());
			return new ResponseEntity<>(userData, HttpStatus.OK);
		}
		throw new OTPException("Some problem is there please check your username and password");
	}

	@PostMapping("/forgetPassword")
	public ResponseEntity<UserRegistrationLoader> forgetPassword(@RequestBody UserRegistrationLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.forgetPassword(userReg), HttpStatus.OK);
	}

	@PostMapping("/forgetEmail")
	public ResponseEntity<UserRegistrationLoader> forgetEmail(@RequestBody UserRegistrationLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.forgetEmail(userReg), HttpStatus.OK);
	}
	
	@PostMapping("/resendOtp")
	public ResponseEntity<UserRegistrationLoader> resendOTP(@RequestBody UserRegistrationLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.resendOTP(userReg),HttpStatus.OK);
	}

	@PostMapping("/forgetPasswordSet")
	public ResponseEntity<UserRegistrationLoader> forgetPasswordSet(@Valid @RequestBody ForgetPasswordLoader userReg) {
		return new ResponseEntity<UserRegistrationLoader>(userServ.forgetPasswordSet(userReg), HttpStatus.OK);
	}

	@GetMapping("/destroy-user-auth")
	public ResponseEntity<String> destroySession(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ResponseEntity<>("Logout user Successfully", HttpStatus.OK);
	}
	
}
