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
					new Role(Long.valueOf(1),"ROLE_ADMIN"),
		            new Role(Long.valueOf(2),"ROLE_MANAGER"),
		            new Role(Long.valueOf(3),"ROLE_STAFF"),
		            new Role(Long.valueOf(4),"ROLE_CANDIDATE")
          )); 
		}
	}
}
