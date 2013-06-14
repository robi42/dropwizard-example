package com.example.helloworld.resources;

import com.codahale.dropwizard.auth.Auth;
import com.example.helloworld.domain.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface ProtectedResource {

    @GET
    String showSecret(@Auth User user);

    @GET
    @Path("/not")
    String not();
}
