package com.example.helloworld.db;

import com.example.helloworld.domain.Models;
import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class TestPersonRepository extends AbstractTransactionalTests {

    private static final String ID = "id";
    private static final String CREATED_AT = "createdAt";

    private final PersonRepository repository = new PersonRepositoryImpl(sessionFactory);

    public TestPersonRepository() throws Exception {
    }


    @Test
    public void createsPerson() throws Exception {
        final Person person = Models.createTestPerson();

        assertThat(repository.create(person))
                .isLenientEqualsToByIgnoringFields(person, ID, CREATED_AT);
    }

    @Test
    public void findsPersonById() throws Exception {
        final Person person = Models.createTestPerson();
        repository.create(person);

        final Optional<Person> persistedPerson = repository.findById(person.getId());

        assertThat(persistedPerson.get())
                .isLenientEqualsToByIgnoringFields(person, ID, CREATED_AT);
    }

    @Test
    public void findsAllEntities() throws Exception {
        final Person person = Models.createTestPerson();
        repository.create(person);

        final Person anotherPerson = Models.createTestPerson();
        repository.create(anotherPerson);

        final List<Person> allEntities = repository.findAll();

        assertThat(allEntities.size()).isEqualTo(2);
    }
}
