package com.me.profile_service.controller;

import com.me.profile_service.constants.ApplicationUrl;
import com.me.profile_service.dto.ApiResponse;
import com.me.profile_service.dto.request.AuthenticationRequestDto;
import com.me.profile_service.dto.request.RegistrationDto;
import com.me.profile_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationUrl.AUTH_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(ApplicationUrl.LOGIN)
    public ApiResponse login(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto){
        log.debug("[+] Inside AuthenticationController.login with dto: {}","Sa");
        ApiResponse response = authenticationService.login(authenticationRequestDto);
        log.debug("[+] AuthenticationController.login response dto: {}", response);
        return response;
    }

    @PostMapping(ApplicationUrl.REGISTER)
    public ApiResponse register(@Valid @RequestBody RegistrationDto registrationDto){
        log.debug("[+] Inside AuthenticationController.register with dto: {}","Sa");
        ApiResponse response = authenticationService.register(registrationDto);
        log.debug("[+] AuthenticationController.register response dto: {}", response);
        return response;
    }
}
