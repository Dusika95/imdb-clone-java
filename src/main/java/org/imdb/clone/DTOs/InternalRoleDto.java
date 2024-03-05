package org.imdb.clone.DTOs;

public enum InternalRoleDto {
    Editor("Editor"),
    Moderator("Moderator");
    private final String value;
    InternalRoleDto(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
