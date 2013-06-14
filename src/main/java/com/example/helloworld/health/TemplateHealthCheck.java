package com.example.helloworld.health;

import com.codahale.metrics.health.HealthCheck;
import com.example.helloworld.domain.Template;
import com.google.common.base.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import static com.example.helloworld.HelloWorldGuiceModule.DEFAULT_TEMPLATE;

public class TemplateHealthCheck extends HealthCheck {

    private final Template template;


    @Inject
    public TemplateHealthCheck(final @Named(DEFAULT_TEMPLATE) Template template) {
        this.template = template;
    }


    @Override
    protected Result check() throws Exception {
        template.render(Optional.of("woo"));
        template.render(Optional.<String>absent());

        return Result.healthy();
    }
}
