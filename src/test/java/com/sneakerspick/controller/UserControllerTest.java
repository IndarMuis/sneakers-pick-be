package com.sneakerspick.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneakerspick.dto.request.RegisterUserRequest;
import com.sneakerspick.dto.response.UserResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception{
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("indar");
        request.setUsername("indar");
        request.setEmail("indar@gmail.com");
        request.setPassword("123");
        request.setPhone("082123456782");

        mockMvc.perform(
                post("/api/v1/users/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), WebResponse.class);
           Assertions.assertNotNull(response.getData());
           Assertions.assertEquals("success", response.getMessage());
           Assertions.assertEquals(201, response.getCode());

           Assertions.assertNull(response.getErrors());

            System.out.println("RESPONSE CODE : " + response.getCode());
            System.out.println("RESPONSE MESSAGE : " + response.getMessage());
            System.out.println("RESPONSE DATA : " + response.getData());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("indar");
        request.setUsername("indar");
        request.setEmail("indar");
        request.setPassword("123");
        request.setPhone("08080808080812121");

        mockMvc.perform(
                post("/api/v1/users/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), WebResponse.class);
            Assertions.assertNull(response.getData());
            Assertions.assertNotNull(response.getErrors());
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getCode());

            System.out.println("RESPONSE CODE : " + response.getCode());
            System.out.println("RESPONSE MESSAGE : " + response.getMessage());
            System.out.println("RESPONSE ERROR : " + response.getErrors());
        });

    }
}