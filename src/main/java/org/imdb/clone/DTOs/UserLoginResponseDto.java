package org.imdb.clone.DTOs;

import org.imdb.clone.models.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserLoginResponseDto {
    private Long id;
    private String nickName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String jwtToken;

    public UserLoginResponseDto() {
    }

    public UserLoginResponseDto(Long id, String nickName, String email, Role role, String jwtToken) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.role = role;
        this.jwtToken = jwtToken;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.nickName = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
