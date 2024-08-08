package com.dhiraj.exception;

public class UserRegistrationOrLoginExcption extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UserRegistrationOrLoginExcption() {
		super();
	}

	public UserRegistrationOrLoginExcption(String message) {
		super(message);
	}

	
}
