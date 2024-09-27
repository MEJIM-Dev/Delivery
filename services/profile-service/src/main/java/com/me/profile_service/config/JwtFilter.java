package com.me.profile_service.config;

import com.me.profile_service.constants.ApplicationUrl;
import com.me.profile_service.model.AppUser;
import com.me.profile_service.repository.AppUserRepository;
import com.me.profile_service.security.JwtService;
import com.me.profile_service.util.UserUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AppUserRepository appUserRepository;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith(ApplicationUrl.AUTH_BASE_URL)){
            filterChain.doFilter(request,response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        if(authorization==null || !authorization.substring(0,7).matches("Bearer ")) {
            doFilter(request, response, filterChain);
            return;
        }

        String jwt = authorization.substring(8);
        String subject = jwtService.getSubject(jwt);

        if(subject!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            Optional<AppUser> optionalAppUser = appUserRepository.findByLogin(subject);
            if (optionalAppUser.isEmpty()) {
                throw new JwtException("Invalid Access Token");
            }

            AppUser appUser = optionalAppUser.get();
            boolean validateJwt = jwtService.validateJwt(jwt, appUser);

            if (validateJwt) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(appUser.getLogin(),jwt, UserUtil.getUserRoles(appUser));
                authenticationToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        doFilter(request,response,filterChain);
    }
}
