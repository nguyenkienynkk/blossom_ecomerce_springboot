package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.models.Role;
import com.ngkk.webapp_springboot.repositories.RoleRepository;
import com.ngkk.webapp_springboot.services.impl.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
