package com.sneakerspick.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.security.jwt.JwtConfig;
import com.sneakerspick.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final JwtConfig jwtConfig;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("START JWT AUTHENTICATION INTERNAL FILTER");

        var header = request.getHeader(jwtConfig.getHeader());

        if (header != null && !header.isBlank() && header.startsWith(jwtConfig.getPrefix())) {
            var accessToken = header.substring(jwtConfig.getPrefix().length());

            try {
                if (jwtService.isValidToken(accessToken)) {
                    Claims claims = jwtService.extractClaims(accessToken);
                    var username = claims.getSubject();
                    List<String> authorities = claims.get("authorities", List.class);

                    if (username != null) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                                );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (Exception exc) {
                log.error("{}", exc.getLocalizedMessage());

                WebResponse<?> webResponse = WebResponse
                        .builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("unauthorized")
                        .build();

                String jsonResponse = objectMapper.writeValueAsString(webResponse);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(jsonResponse);
                return;
            }
        }
        log.info("Do filter {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
