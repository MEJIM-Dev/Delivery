package com.me.profile_service.dto;

import lombok.Data;

@Data
public class ApiResponse <G> {
    private String status;
    private String message;
    private G data;
}
