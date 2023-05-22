package com.sneakerspick.configs;

import com.sneakerspick.security.jwt.JWTAuthHelper;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Key;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
						.requestMatchers("/v1/auth").permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf().disable()
				.httpBasic();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Key key() {
		byte[] bytes = Decoders.BASE64.decode(JWTAuthHelper.SECRET_KEY);
		return Keys.hmacShaKeyFor(bytes);
	}

}
