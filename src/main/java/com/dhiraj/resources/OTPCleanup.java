
package com.dhiraj.resources;

import java.util.Map;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.dhiraj.model.UserRegistration;
import com.dhiraj.repository.UserRegistrationRepository;

@Configuration
public class OTPCleanup {

	@Autowired
	private UserRegistrationRepository userRegRepo;

	@Value("${spring.mail.otp.cleanup.time}")
	private int cleanupTime;

	private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void cleanup(String email) {
		cancelExistingTask(email);

		ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> executeTask(email), cleanupTime, TimeUnit.MINUTES);
		scheduledTasks.put(email, scheduledFuture);
	}

	private void executeTask(String email) {
		try {
			UserRegistration user = userRegRepo.findByEmail(email);
			if (user != null && user.getOtp() != null) {
				user.setOtp(null);
				userRegRepo.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scheduledTasks.remove(email);
		}
	}

	private void cancelExistingTask(String email) {
		ScheduledFuture<?> existingTask = scheduledTasks.get(email);
		if (existingTask != null) {
			existingTask.cancel(true);
			scheduledTasks.remove(email);
		}
	}
}
