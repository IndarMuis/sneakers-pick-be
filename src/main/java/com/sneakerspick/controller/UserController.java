package com.sneakerspick.controller;

import com.sneakerspick.dto.request.RegisterRequest;
import com.sneakerspick.dto.response.UserResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return WebResponse.<UserResponse>builder()
                .message("success")
                .code(HttpStatus.CREATED.value())
                .data(response).build();
    }

}
