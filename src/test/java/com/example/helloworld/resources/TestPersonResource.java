package com.example.helloworld.resources;

import com.codahale.dropwizard.testing.ResourceTest;
import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.UUID;
import java.util.logging.Logger;

import static com.codahale.dropwizard.testing.FixtureHelpers.fixture;
import static java.util.TimeZone.getTimeZone;
import static java.util.logging.Level.OFF;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.joda.time.DateTimeZone.UTC;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestPersonResource extends ResourceTest {
    static {
        Logger.getLogger("com.sun.jersey").setLevel(OFF);
    }

    private static final String PERSON_ID = "9b89ec69-e844-4e88-ada6-5ee93e947a16";
    private static final String PERSON_PATH = "/people/" + PERSON_ID;

    private final Person person = createTestPerson();

    private Person createTestPerson() {
        final Person person = new Person();
        person.setId(UUID.fromString(PERSON_ID));
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setCreatedAt(new DateTime(1371208810893L).withZone(UTC));
        person.setBirthday(DateTime.parse("1980-1-1").withZone(UTC).toDate());
        person.setTimeZonePreference(getTimeZone(UTC.getID()));
        return person;
    }

    private final PersonRepository repository = mock(PersonRepository.class);


    @Override
    protected void setUpResources() throws Exception {
        when(repository.findById(UUID.fromString(PERSON_ID)))
                .thenReturn(Optional.of(person));

        addResource(new PersonResourceImpl(repository));
    }


    @Test
    public void getsPersonById() throws Exception {
        assertThat(client().resource(PERSON_PATH)
                .accept(APPLICATION_JSON)
                .get(Person.class))
                .isEqualTo(person);
    }

    @Test
    public void getsCorrectJson() throws Exception {
        final String expectedJson = fixture("fixtures/person.json").replaceAll("\\s", "");

        assertThat(client().resource(PERSON_PATH)
                .accept(APPLICATION_JSON)
                .get(String.class))
                .isEqualTo(expectedJson);
    }
}
