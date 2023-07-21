package com.sneakerspick.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.MockMvc.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testNotFoundProduct() throws Exception {
        mockMvc.perform(
                get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbmRhcm11aXMiLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImlhdCI6MTY4OTkwMjUyNCwiZXhwIjoxNjg5OTAyNjQ0fQ.kA6dbdMOwzQPtO60tLZOI75VYQBdYGVWO7VeH5M5wbY")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<?> webResponse =  objectMapper.readValue(result.getResponse().getContentAsString(), WebResponse.class);
            Assertions.assertNull(webResponse.getData());
        });
    }
}