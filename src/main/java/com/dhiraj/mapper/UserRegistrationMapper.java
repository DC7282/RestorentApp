package com.dhiraj.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dhiraj.dto.UserRegistrationLoader;
import com.dhiraj.model.UserRegistration;

@Component
public class UserRegistrationMapper {

	@Autowired
	ModelMapper modelMapper;
	
	public UserRegistration loaderToUserRegistration(UserRegistrationLoader loader) {
		return modelMapper.map(loader, UserRegistration.class);
	}
	
	public UserRegistrationLoader userRegistrationToLoader(UserRegistration registration) {
		return modelMapper.map(registration, UserRegistrationLoader.class);
	}
	
}
