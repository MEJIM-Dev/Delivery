package com.me.profile_service.security;

import com.me.profile_service.model.AppUser;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String getSubject(String token);

    String generateJwt(AppUser userDetails);

    String generateJwt(
            AppUser user,
            Map<String, Object> extraClaims
    );

    boolean validateJwt(String token, AppUser userDetails);

    boolean tokenIsNonExpired(String token);

    Date extractExpiration(String token);

    <T> T getClaim (String token, Function<Claims, T> claimsResolver);

    Claims getAllClaims(String token);

    Key getSignInKey();
}
