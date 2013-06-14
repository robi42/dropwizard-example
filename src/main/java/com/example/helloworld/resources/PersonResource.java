package com.example.helloworld.resources;

import com.codahale.dropwizard.hibernate.UnitOfWork;
import com.example.helloworld.domain.Person;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import java.util.UUID;

public interface PersonResource {

    @GET
    @UnitOfWork
    Person getPerson(@PathParam("personId") UUID personId);
}
