package org.imdb.clone.DTOs;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserLoginDto {
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotEmpty(message = "Password is required.")
    private String password;

    public UserLoginDto() {}

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
