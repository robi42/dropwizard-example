package com.example.helloworld.resources;

import com.codahale.dropwizard.jersey.caching.CacheControl;
import com.codahale.metrics.annotation.Timed;
import com.example.helloworld.domain.Saying;
import com.google.common.base.Optional;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import static java.util.concurrent.TimeUnit.DAYS;

public interface HelloWorldResource {

    @GET
    @Timed(name = "get-requests")
    @CacheControl(maxAge = 1, maxAgeUnit = DAYS)
    Saying sayHello(@QueryParam("name") Optional<String> name);

    @POST
    void receiveHello(@Valid Saying saying);
}
