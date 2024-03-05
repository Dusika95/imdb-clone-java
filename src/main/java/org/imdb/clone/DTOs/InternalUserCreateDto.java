package org.imdb.clone.DTOs;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class InternalUserCreateDto {
    @NotBlank(message = "Name is required")
    private String nickName;
    @NotBlank(message = "Name is required")
    @Email(message = "Invalid email format.")
    private String email;
    @Enumerated(EnumType.STRING)
    private InternalRoleDto role;
    @NotBlank(message = "Password is required.")
    @Size(min = 3, message = "Password must be at least 3 characters.")
    private String password;
    @NotBlank(message = "Password is required.")
    @Size(min = 3, message = "Password must be at least 3 characters.")
    private String passwordConfirm;

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(passwordConfirm);
    }

    public InternalUserCreateDto() {
    }

    public InternalUserCreateDto(String nickName, String email, InternalRoleDto role, String password, String passwordConfirm) {
        this.nickName = nickName;
        this.email = email;
        this.role = role;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InternalRoleDto getRole() {
        return role;
    }

    public void setRole(InternalRoleDto role) {
        this.role = role;
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
