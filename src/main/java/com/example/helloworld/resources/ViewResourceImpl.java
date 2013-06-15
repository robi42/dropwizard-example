package com.example.helloworld.resources;

import com.codahale.dropwizard.views.View;

import javax.inject.Singleton;

import static com.google.common.base.Charsets.ISO_8859_1;
import static com.google.common.base.Charsets.UTF_8;

@Singleton
public class ViewResourceImpl implements ViewResource {

    @Override
    public View freemarkerUtf8() {
        return new View("/views/ftl/utf8.ftl", UTF_8) {
        };
    }

    @Override
    public View freemarkerIso88591() {
        return new View("/views/ftl/iso88591.ftl", ISO_8859_1) {
        };
    }

    @Override
    public View mustacheUtf8() {
        return new View("/views/mustache/utf8.mustache", UTF_8) {
        };
    }

    @Override
    public View mustacheIso88591() {
        return new View("/views/mustache/iso88591.mustache", ISO_8859_1) {
        };
    }
}
