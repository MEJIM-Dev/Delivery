package com.me.profile_service.dto.request;

import com.me.profile_service.model.enumeration.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationDto {
    @Email
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^([0-9]|\\+)[0-9]{10,20}$")
    private String mobile;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[A-Za-z][a-zA-Z]{1,50}$")
    @Size(min = 2, max = 50)
    private String firstname;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[A-Za-z][a-zA-Z]{1,50}$")
    @Size(min = 2, max = 50)
    private String lastname;
    private String otherNames;
    @NotNull
    private Gender sex;
    private String token;
}
