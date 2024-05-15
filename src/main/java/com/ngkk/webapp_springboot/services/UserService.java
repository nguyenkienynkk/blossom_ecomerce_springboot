package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.components.JwtTokenUtils;
import com.ngkk.webapp_springboot.components.LocalizationUtils;
import com.ngkk.webapp_springboot.dtos.UpdateUserDTO;
import com.ngkk.webapp_springboot.dtos.UserDTO;
import com.ngkk.webapp_springboot.exceptions.DataNotFoundException;
import com.ngkk.webapp_springboot.exceptions.ExpiredTokenException;
import com.ngkk.webapp_springboot.exceptions.PermissionDenyException;
import com.ngkk.webapp_springboot.models.Role;
import com.ngkk.webapp_springboot.models.User;
import com.ngkk.webapp_springboot.repositories.RoleRepository;
import com.ngkk.webapp_springboot.repositories.UserRepository;
import com.ngkk.webapp_springboot.services.impl.IUserService;
import com.ngkk.webapp_springboot.utils.MessageKeys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService implements IUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenUtils jwtTokenUtil;
    AuthenticationManager authenticationManager;
    LocalizationUtils localizationUtils;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        //register the user
        String phoneNumber = userDTO.getPhoneNumber();
        //Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("Phone number already exists");

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if (role.getName().toUpperCase().equals(Role.ADMIN))
            throw new PermissionDenyException("You cannot register an admin account");

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
        newUser.setRole(role);
        //Kiểm tra nếu có accountId, không yêu cầu password
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String passwordEncoded = passwordEncoder.encode(password);
            newUser.setPassword(passwordEncoded);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        User existingUser = optionalUser.get();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,
                password,
                existingUser.getAuthorities()
        );
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
            }
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS));
        }
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }

    }

    @Transactional
    @Override
    public User updateUser(Long userId, UpdateUserDTO updatedUserDTO ) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        String newPhoneNumber = updatedUserDTO.getPhoneNumber();
        if (!existingUser.getPhoneNumber().equals(newPhoneNumber) &&
                userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exitsts");
        }

        //Update user information based on the DTO
        //Update user information based on the DTO,only if the DTO field is not null
        if (updatedUserDTO.getFullName() != null) {
            existingUser.setFullName(updatedUserDTO.getFullName());
        }
        if (newPhoneNumber != null) {
            existingUser.setPhoneNumber(newPhoneNumber);
        }
        if (updatedUserDTO.getAddress() != null) {
            existingUser.setAddress(updatedUserDTO.getAddress());
        }
        if (updatedUserDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updatedUserDTO.getDateOfBirth());
        }
        if (updatedUserDTO.getGoogleAccountId() > 0) {
            existingUser.setGoogleAccountId(updatedUserDTO.getGoogleAccountId());
        }
        if (updatedUserDTO.getFacebookAccountId() > 0) {
            existingUser.setFacebookAccountId(updatedUserDTO.getFacebookAccountId());
        }
        if(updatedUserDTO.getPassword() != null &&
                !updatedUserDTO.getPassword().isEmpty()) {
            String newPassword = updatedUserDTO.getPassword();
            String encoderPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encoderPassword);
        }
        return userRepository.save(existingUser);

    }
}
