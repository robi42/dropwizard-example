package com.example.helloworld;

import com.codahale.dropwizard.Configuration;
import com.codahale.dropwizard.db.DataSourceFactory;
import com.example.helloworld.domain.Template;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class HelloWorldConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String templateContent;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";

    @NotEmpty
    @JsonProperty
    private String appBasicAuthUsername = "j.doe@example.com";

    @NotEmpty
    @JsonProperty
    private String appBasicAuthPassword = "secret";

    @NotEmpty
    @JsonProperty
    private String adminBasicAuthUsername = "admin";

    @NotEmpty
    @JsonProperty
    private String adminBasicAuthPassword = "admin";

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();


    public Template createTemplate() {
        return new Template(templateContent, defaultName);
    }
}
