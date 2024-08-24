package com.dhiraj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dhiraj.model.Role;
import com.dhiraj.repository.RoleRepository;

@Component
public class TableCreationRunner implements CommandLineRunner{
	
	@Autowired
	RoleRepository roleRepo;
	
	@Override
	public void run(String... args) throws Exception {
		if (roleRepo.count() == 0) {
			roleRepo.saveAll(List.of(
					new Role(Long.valueOf(1),"ADMIN"),
		            new Role(Long.valueOf(2),"MANAGER"),
		            new Role(Long.valueOf(3),"STAFF"),
		            new Role(Long.valueOf(4),"DELIVERY_PARTNER"),
		            new Role(Long.valueOf(4),"CANDIDATE")
          )); 
		}
	}
}
