package com.example.helloworld.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class User {

    @Getter
    private final String name;


    public User(final String name) {
        this.name = name;
    }
}
