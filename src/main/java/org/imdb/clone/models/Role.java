package org.imdb.clone.models;

public enum Role {
    User("User"),
    Editor("Editor"),
    Moderator("Moderator"),
    Admin("Admin");
    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
