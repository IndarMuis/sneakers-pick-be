package com.sneakerspick.controller;

import com.sneakerspick.dto.request.RegisterRequest;
import com.sneakerspick.dto.request.UpdateUserRequest;
import com.sneakerspick.dto.response.UserResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(
            path = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> currentLogged() {
        UserResponse response = userService.getCurrentLoggedUser();
        return WebResponse.<UserResponse>builder()
                .message("success")
                .code(HttpStatus.OK.value())
                .data(response).build();
    }

    @PatchMapping(
            path = "/update/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable(name = "id") Long id) {
        UserResponse response = userService.update(request, id);

        return WebResponse.<UserResponse>builder()
                .message("success")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();
    }

}
