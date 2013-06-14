package com.example.helloworld.resources;

import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.domain.Person;
import com.google.inject.Inject;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResourceImpl implements PeopleResource {

    private final PersonRepository repository;


    @Inject
    public PeopleResourceImpl(final PersonRepository repository) {
        this.repository = repository;
    }


    public Person createPerson(final Person person) {
        return repository.create(person);
    }

    public List<Person> listPeople() {
        return repository.findAll();
    }
}
