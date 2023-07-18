package com.sneakerspick.service;

import com.sneakerspick.dto.request.RegisterUserRequest;
import com.sneakerspick.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterUserRequest request);

    UserResponse findByUsername(String username);

}
