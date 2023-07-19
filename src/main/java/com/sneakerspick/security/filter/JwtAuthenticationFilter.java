package com.sneakerspick.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneakerspick.dto.request.LoginRequest;
import com.sneakerspick.dto.response.LoginResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.security.CustomUserDetail;
import com.sneakerspick.security.CustomUserDetailService;
import com.sneakerspick.security.jwt.JwtConfig;
import com.sneakerspick.security.jwt.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    private JwtService jwtService;

    private JwtConfig jwtConfig;

    private CustomUserDetailService customUserDetailService;

    public JwtAuthenticationFilter(
            ObjectMapper objectMapper,
            JwtService jwtService,
            JwtConfig jwtConfig,
            CustomUserDetailService customUserDetailService,
            AuthenticationManager authenticationManager
    ) {
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
        this.customUserDetailService = customUserDetailService;

        // TODO: 7/19/2023 TESTING
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid username or password");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        // TODO: 7/19/2023 TESTING
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        String accessToken = jwtService.generateToken(customUserDetail);

        LoginResponse loginResponse = new LoginResponse(
                customUserDetail.getName(),
                customUserDetail.getEmail(),
                accessToken
        );
        String jsonResponse = objectMapper.writeValueAsString(loginResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("INVALID AUTHENTICATION");
        WebResponse<?> webResponse = WebResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("unauthorized")
                .build();
        String jsonResponse = objectMapper.writeValueAsString(webResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}
