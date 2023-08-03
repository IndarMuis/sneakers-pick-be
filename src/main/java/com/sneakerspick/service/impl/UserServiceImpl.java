package com.sneakerspick.service.impl;

import com.sneakerspick.domain.Role;
import com.sneakerspick.domain.User;
import com.sneakerspick.dto.request.RegisterRequest;
import com.sneakerspick.dto.request.UpdateUserRequest;
import com.sneakerspick.dto.response.UserResponse;
import com.sneakerspick.repository.RoleRepository;
import com.sneakerspick.repository.UserRepository;
import com.sneakerspick.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final RoleRepository roleRepository;


    @Transactional
    @Override
    public UserResponse register(RegisterRequest request) {
        validationService.validate(request);
        if (userRepository.existsUserByUsername(request.getUsername())) {
            log.error("USER ALREADY EXIST");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
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
        userRepository.save(user);

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

    @Override
    public UserResponse getCurrentLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) auth.getPrincipal();
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        return UserResponse.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .username(username)
                .email(user.get().getEmail())
                .phone(user.get().getPhone())
                .build();
    }

    @Override
    @Transactional
    public UserResponse update(UpdateUserRequest request, Long id) {
        validationService.validate(request);

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        if (Objects.nonNull(request.getName())) {
            user.get().setName(request.getName());
        }

        if (Objects.nonNull(request.getPhone())) {
            user.get().setPhone(request.getPhone());
        }

        userRepository.save(user.get());
        return UserResponse.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .username(user.get().getUsername())
                .email(user.get().getEmail())
                .phone(user.get().getPhone())
                .build();
    }

}
