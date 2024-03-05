package org.imdb.clone.DTOs;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserUpdateDto {
    @NotBlank
    @Email(message = "Invalid email format.")
    private String email;
    private String password;
    private String passwordConfirm;

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        return password == null || passwordConfirm == null || password.equals(passwordConfirm);
    }

    public UserUpdateDto() {
    }

    public UserUpdateDto(String password, String passwordConfirm) {
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    public UserUpdateDto(String email) {
        this.email = email;
    }
    public UserUpdateDto(String email, String password, String passwordConfirm) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
