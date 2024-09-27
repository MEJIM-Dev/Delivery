package com.me.profile_service.dto.response;

import lombok.Data;

@Data
public class AuthenticationToken {
    private String access_token;
    private String refresh_token;
}
