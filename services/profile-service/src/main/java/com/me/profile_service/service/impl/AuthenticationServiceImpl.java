package com.me.profile_service.service.impl;

import com.me.profile_service.constants.ExtendedConstants;
import com.me.profile_service.dto.ApiResponse;
import com.me.profile_service.dto.request.AuthenticationRequestDto;
import com.me.profile_service.dto.request.RegistrationDto;
import com.me.profile_service.dto.response.AuthenticationToken;
import com.me.profile_service.model.AppUser;
import com.me.profile_service.model.Role;
import com.me.profile_service.model.enumeration.Status;
import com.me.profile_service.repository.AppUserRepository;
import com.me.profile_service.repository.RoleRepository;
import com.me.profile_service.security.JwtService;
import com.me.profile_service.service.AuthenticationService;
import com.me.profile_service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AppUserRepository appUserRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Value("${app.login.pattern:0}")
    private int loginPattern;

    @Value("${app.login.requiresActivation:false}")
    private boolean requiresActivation;

    private final RoleRepository roleRepository;

    @Override
    public ApiResponse login(AuthenticationRequestDto authenticationRequestDto) {
        var apiResponse = new ApiResponse();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<AppUser> optionalAppUser = appUserRepository.findByLogin(authenticationRequestDto.getUsername());
            if (optionalAppUser.isEmpty()) {
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            AppUser user = optionalAppUser.get();
            AuthenticationToken authenticationToken = authenticateUser(authenticationRequestDto.getPassword(), user);

            apiResponse.setData(authenticationToken);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public ApiResponse register(RegistrationDto registrationDto) {
        var apiResponse = new ApiResponse();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<AppUser> existingUser = appUserRepository.findByLogin(registrationDto.getEmail());
            if(existingUser.isPresent()){
                apiResponse.setMessage(ExtendedConstants.ResponseCode.DUPLICATE_RECORD.getMessage());
                return apiResponse;
            }

            AppUser user = registrationDtoToUser(registrationDto);
            user = appUserRepository.save(user);

            AuthenticationToken authenticationToken = authenticateUser(registrationDto.getPassword(), user);

            apiResponse.setData(authenticationToken);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    private AuthenticationToken authenticateUser(String password, AppUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getLogin(), password);
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtService.generateJwt(user, UserUtil.getExtraClaims(user));
        AuthenticationToken responseToken = new AuthenticationToken();
        responseToken.setAccess_token(accessToken);

        return responseToken;
    }


    private AppUser registrationDtoToUser(RegistrationDto registrationDto) {
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registrationDto,appUser);
        if(requiresActivation){
            appUser.setStatus(Status.INACTIVE);
        } else {
            appUser.setStatus(Status.ACTIVE);
        }

        if(loginPattern==0) {
            appUser.setLogin(registrationDto.getEmail());
        } else if(loginPattern==1){
            appUser.setLogin(registrationDto.getMobile());
        } else {
            appUser.setLogin(registrationDto.getEmail());
        }

        Optional<Role> optionalRole = roleRepository.findByName("USER");
        appUser.setRoles(new HashSet<>(Set.of(optionalRole.get())));

        return appUser;
    }
}
