package com.sneakerspick.security.jwt;

import com.sneakerspick.domain.Role;
import com.sneakerspick.security.CustomUserDetail;
import com.sneakerspick.security.CustomUserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService extends JwtConfig {

    private final CustomUserDetailService customUserDetailService;

    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(CustomUserDetail customUserDetail) {
        List<String> authorities = new ArrayList<>();
        customUserDetail.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));

        Instant currentDateTime = Instant.now();
        return Jwts.builder()
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", "JWT")
                .setSubject(customUserDetail.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(Date.from(currentDateTime))
                .setExpiration(Date.from(currentDateTime.plusSeconds(getExpiration() * 1000)))
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException exc) {
            log.info("unsupported jwt token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unsupported JWT Token");
        } catch (MalformedJwtException exc) {
            log.info("malformed jwt token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Malformed JWT Token");
        } catch (ExpiredJwtException exc) {
            log.info("expired jwt token");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired JWT Token");
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown Error");
        }
    }

}
