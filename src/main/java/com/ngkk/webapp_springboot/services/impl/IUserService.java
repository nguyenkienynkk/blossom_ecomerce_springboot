package com.ngkk.webapp_springboot.services.impl;

import com.ngkk.webapp_springboot.dtos.UpdateUserDTO;
import com.ngkk.webapp_springboot.dtos.UserDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.exceptions.PermissionDenyException;
import com.ngkk.webapp_springboot.models.User;
import org.springframework.stereotype.Service;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password,Long roleId) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;
    User updateUser(Long userId, UpdateUserDTO updatedUserDTO ) throws Exception;
}
