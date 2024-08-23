package com.dhiraj.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(UserRegistrationOrLoginExcption.class)
	public ResponseEntity<ErrorResponse> handleUserRegistrationOrLoginExcption(UserRegistrationOrLoginExcption ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(OTPException.class)
	public ResponseEntity<ErrorResponse> handleOTPException(OTPException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(UnexpectedException.class)
	public ResponseEntity<ErrorResponse> handleUnexpectedException(UnexpectedException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		Map<String, ErrorResponse> errResp = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errResp.put(fieldName, new ErrorResponse(message,HttpStatus.BAD_REQUEST));
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResp);
	}
}
