package com.me.profile_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(e-> e.disable())
                .cors(e -> e.disable())
                .authorizeHttpRequests(e->{
                    e.requestMatchers("/")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/v1/auth/**")
                            .permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/users","/api/v1/user/**","/api/v1/posts","/api/v1/post/**","/api/v1/comment/**")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/v1/register")
                            .permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/comments")
                            .hasRole("ADMIN")
                            .anyRequest()
                            .authenticated()
                    ;
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(e->{
                    e.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }
}
