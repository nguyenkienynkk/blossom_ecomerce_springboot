package com.ngkk.webapp_springboot.services.impl;

import com.ngkk.webapp_springboot.dtos.UserDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.User;
import org.springframework.stereotype.Service;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password) throws Exception;
}
