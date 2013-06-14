package com.example.helloworld.resources;

import com.codahale.dropwizard.hibernate.UnitOfWork;
import com.codahale.metrics.annotation.Timed;
import com.example.helloworld.domain.Person;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PeopleResource {

    @POST
    @UnitOfWork
    Person createPerson(Person person);

    @GET
    @UnitOfWork
    @Timed(name = "get-people-requests")
    List<Person> listPeople();
}
