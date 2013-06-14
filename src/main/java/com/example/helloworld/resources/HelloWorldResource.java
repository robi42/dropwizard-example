package com.example.helloworld.resources;

import com.codahale.dropwizard.jersey.caching.CacheControl;
import com.codahale.metrics.annotation.Timed;
import com.example.helloworld.domain.Saying;
import com.google.common.base.Optional;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.util.concurrent.TimeUnit.DAYS;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface HelloWorldResource {

    @GET
    @Timed(name = "get-requests")
    @CacheControl(maxAge = 1, maxAgeUnit = DAYS)
    Saying sayHello(@QueryParam("name") Optional<String> name);

    @POST
    void receiveHello(@Valid Saying saying);
}
