package com.example.helloworld.resources;

import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.domain.Person;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
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
