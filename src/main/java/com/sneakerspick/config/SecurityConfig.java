package com.sneakerspick.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneakerspick.exception.CustomAccessDeniedHandler;
import com.sneakerspick.security.CustomUserDetailService;
import com.sneakerspick.security.filter.JwtAuthenticationFilter;
import com.sneakerspick.security.filter.UsernamePasswordAuthProcessingFilter;
import com.sneakerspick.security.filter.UsernamePasswordAuthProvider;
import com.sneakerspick.security.jwt.JwtConfig;
import com.sneakerspick.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {

    private CustomUserDetailService customUserDetailService;
    private JwtConfig jwtConfig;
    private JwtService jwtService;
    private ObjectMapper objectMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

    @Autowired
    public void registerAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("START SECURITY FILTER CHAIN");

        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = managerBuilder.build();

        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        auth ->
                        {
                            auth.requestMatchers("/api/v1/users/register", "/api/v1/auth/login").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .authenticationManager(authenticationManager)
                .exceptionHandling(handler -> {
                    handler.authenticationEntryPoint(
                            (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                    );
                    handler.accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper));
                })
                .addFilterBefore(
                        new UsernamePasswordAuthProcessingFilter(objectMapper, jwtService, jwtConfig, customUserDetailService, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        new JwtAuthenticationFilter(jwtService, jwtConfig, objectMapper),
                        UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

}
