package com.dhiraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhiraj.model.UserRegistration;

@Repository("userRegRepo")
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

	UserRegistration findByEmail(String email);

	UserRegistration findByEmailAndPassword(String email, String password);

}
