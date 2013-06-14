package com.example.helloworld.resources;

import com.codahale.dropwizard.hibernate.UnitOfWork;
import com.codahale.metrics.annotation.Timed;
import com.example.helloworld.domain.Person;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.List;

public interface PeopleResource {

    @POST
    @UnitOfWork
    Person createPerson(Person person);

    @GET
    @UnitOfWork
    @Timed(name = "get-people-requests")
    List<Person> listPeople();
}
