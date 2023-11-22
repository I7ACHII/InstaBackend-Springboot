package com.geekster.InstagramBackend.Model.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInAdmin {
    @Pattern(regexp = "^.+@admin\\.com$")
    private String email;
    private String password;
}
