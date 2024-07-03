package com.dhiraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dhiraj.model.Role;

@Repository("roleRepo")
public interface RoleRepository extends JpaRepository<Role, Long>{

	@Override
	public long count();
}
