package com.ngkk.webapp_springboot.repositories;

import com.ngkk.webapp_springboot.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
