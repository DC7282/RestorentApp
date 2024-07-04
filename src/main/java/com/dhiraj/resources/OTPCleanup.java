package com.dhiraj.resources;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dhiraj.model.UserRegistration;
import com.dhiraj.repository.UserRegistrationRepository;

@Configuration
public class OTPCleanup {

	@Autowired
	private UserRegistrationRepository userRegRepo;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private String email;

	public void cleanup(String email) {
		scheduler.schedule(this::executeTask, 10, TimeUnit.MINUTES);
		this.email = email;
	}

	private void executeTask() {
		try {
			UserRegistration user = userRegRepo.findByEmail(email);
			if(user!=null)
				if (user.getOtp() != null) {
					user.setOtp(null);
					userRegRepo.save(user);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
