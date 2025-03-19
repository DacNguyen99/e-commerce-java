package com.dacnguyen.ecommerce.service.impl;

import com.dacnguyen.ecommerce.dto.general.UserDto;
import com.dacnguyen.ecommerce.dto.request.LoginRequest;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.entity.User;
import com.dacnguyen.ecommerce.enums.UserRole;
import com.dacnguyen.ecommerce.exception.InvalidCredentialsException;
import com.dacnguyen.ecommerce.exception.NotFoundException;
import com.dacnguyen.ecommerce.mapper.EntityToDtoMapper;
import com.dacnguyen.ecommerce.repository.UserRepository;
import com.dacnguyen.ecommerce.security.jwt.JwtUtils;
import com.dacnguyen.ecommerce.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityToDtoMapper entityToDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;

        if (registrationRequest.getRole() != null
                && registrationRequest.getRole().equalsIgnoreCase("admin")) {
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        UserDto userDto = entityToDtoMapper.userToUserDto(savedUser);

        return Response.builder()
                .status(200)
                .message("User registered successfully!")
                .user(userDto)
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password incorrect!");
        }

        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("Login successfully!")
                .token(token)
                .expirationTime("1 Week")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(entityToDtoMapper::userToUserDto).toList();

        return Response.builder()
                .status(200)
                .message("All users found!")
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not found!"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityToDtoMapper.userToUserDtoWithAddressAndOrderHistory(user);

        return Response.builder()
                .status(200)
                .message("Get user info successfully!")
                .user(userDto)
                .build();
    }
}
