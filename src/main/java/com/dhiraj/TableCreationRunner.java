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
					new Role(Long.valueOf(1),"admin"),
		            new Role(Long.valueOf(2),"manager"),
		            new Role(Long.valueOf(3),"staff"),
		            new Role(Long.valueOf(4),"candidate")                
          )); 
		}
	}
}
