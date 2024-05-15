package com.ngkk.webapp_springboot.controllers;

import com.ngkk.webapp_springboot.dtos.UpdateUserDTO;
import com.ngkk.webapp_springboot.dtos.UserLoginDTO;
import com.ngkk.webapp_springboot.models.User;
import com.ngkk.webapp_springboot.responses.LoginResponse;
import com.ngkk.webapp_springboot.responses.RegisterResponse;
import com.ngkk.webapp_springboot.responses.UserResponse;
import com.ngkk.webapp_springboot.services.impl.IUserService;
import com.ngkk.webapp_springboot.components.LocalizationUtils;
import com.ngkk.webapp_springboot.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ngkk.webapp_springboot.dtos.UserDTO;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    IUserService userService;
    LocalizationUtils localizationUtils;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMs = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, errorMs))
                        .build());
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                        .build());
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(
                    RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody UserLoginDTO userDTO) {
        //Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(
                    userDTO.getPhoneNumber(),
                    userDTO.getPassword(),
                    userDTO.getRoleId()
            );
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            //Lấy được token
            String extractedToken = authorizationHeader.substring(7);
            //Tìm ra thông tin user đó
            User user = userService.getUserDetailsFromToken(extractedToken);
            //Lấy ra id rồi so sánh nếu đúng thì mới cho phép update
            //Chỉ có tôi mới được update user của tôi
            if (user.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User updateUser = userService.updateUser(userId, updateUserDTO);
            return ResponseEntity.ok(UserResponse.fromUser(updateUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

