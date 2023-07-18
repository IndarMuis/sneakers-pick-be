package com.sneakerspick.service.impl;

import com.sneakerspick.domain.Role;
import com.sneakerspick.domain.User;
import com.sneakerspick.dto.request.RegisterUserRequest;
import com.sneakerspick.dto.response.UserResponse;
import com.sneakerspick.repository.RoleRepository;
import com.sneakerspick.repository.UserRepository;
import com.sneakerspick.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        validationService.validate(request);
        if (userRepository.existsUserByUsername(request.getUsername())) {
            log.error("USER ALREADY EXIST");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username or email already registered");
        }
        log.info("CREATE USER");
        Role role = roleRepository.findFirstByName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(role));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone()).build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

}
