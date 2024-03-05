package org.imdb.clone.models;

public enum MovieRole {
    Actor("Actor"),
    Director("Director"),
    Writer("Writer"),
    Composer("Composer");
    private final String value;

    MovieRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
