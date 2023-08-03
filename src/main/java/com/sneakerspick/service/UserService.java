package com.sneakerspick.service;

import com.sneakerspick.dto.request.RegisterRequest;
import com.sneakerspick.dto.request.UpdateUserRequest;
import com.sneakerspick.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse findByUsername(String username);

    UserResponse getCurrentLoggedUser();

    UserResponse update(UpdateUserRequest request, Long id);

}
