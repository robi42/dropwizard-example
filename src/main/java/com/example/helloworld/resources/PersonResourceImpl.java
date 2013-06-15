package com.example.helloworld.resources;

import com.codahale.dropwizard.jersey.errors.ErrorMessage;
import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
public class PersonResourceImpl implements PersonResource {

    private final PersonRepository repository;


    @Inject
    public PersonResourceImpl(final PersonRepository repository) {
        this.repository = repository;
    }


    @Override
    public Person getPerson(final UUID personId) {
        final Optional<Person> person = repository.findById(personId);

        if (!person.isPresent()) {
            final String messageText = "No such user";

            throw new WebApplicationException(new NotFoundException(messageText),
                    Response.status(NOT_FOUND)
                            .entity(new ErrorMessage(messageText))
                            .build());
        }

        return person.get();
    }
}
