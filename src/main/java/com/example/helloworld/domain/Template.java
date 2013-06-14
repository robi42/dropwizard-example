package com.example.helloworld.domain;

import com.google.common.base.Optional;

public class Template {

    private final String content;
    private final String defaultName;


    public Template(final String content, final String defaultName) {
        this.content = content;
        this.defaultName = defaultName;
    }


    public String render(final Optional<String> name) {
        return String.format(content, name.or(defaultName));
    }
}
