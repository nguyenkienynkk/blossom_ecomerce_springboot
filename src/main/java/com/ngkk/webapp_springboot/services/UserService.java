package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.UserDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.models.Role;
import com.ngkk.webapp_springboot.models.User;
import com.ngkk.webapp_springboot.repositories.RoleRepository;
import com.ngkk.webapp_springboot.repositories.UserRepository;
import com.ngkk.webapp_springboot.services.impl.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        //Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("Phone number already exists");
        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        //Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
//            String encoderPass = passwordEncoder.encode(password);
            //sẽ nói đến trong phần spring security
            //newUser.setPassword(encoderPass)
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        //làm sau
        return null;
    }
}
