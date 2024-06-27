package com.dhiraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhiraj.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	@Override
	public long count();
}
