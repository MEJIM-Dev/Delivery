package com.me.profile_service.service;

import com.me.profile_service.dto.ApiResponse;
import com.me.profile_service.dto.request.AuthenticationRequestDto;
import com.me.profile_service.dto.request.RegistrationDto;

public interface AuthenticationService {
    
    ApiResponse login(AuthenticationRequestDto authenticationRequestDto);

    ApiResponse register(RegistrationDto registrationDto);
}
