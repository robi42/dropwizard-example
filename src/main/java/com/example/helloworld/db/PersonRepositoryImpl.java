package com.example.helloworld.db;

import com.codahale.dropwizard.hibernate.AbstractDAO;
import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.joda.time.DateTimeZone.UTC;

@Singleton
public class PersonRepositoryImpl extends AbstractDAO<Person>
        implements PersonRepository {

    @Inject
    public PersonRepositoryImpl(final SessionFactory factory) {
        super(factory);
    }


    public Person create(final Person person) {
        person.setId(randomUUID());
        person.setCreatedAt(DateTime.now(UTC));

        return persist(person);
    }

    public Optional<Person> findById(final UUID id) {
        return Optional.fromNullable(get(id));
    }

    public List<Person> findAll() {
        final Query query = currentSession().createQuery("select p from Person p order by createdAt desc");
//        final Query query = currentSession().createSQLQuery("SELECT * FROM people ORDER BY created_at DESC")
//                .addEntity(Person.class);

        return list(query);
    }
}
