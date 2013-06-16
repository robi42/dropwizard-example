package com.example.helloworld.resources;

import com.codahale.dropwizard.testing.ResourceTest;
import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.domain.Models;
import com.example.helloworld.domain.Person;
import com.google.common.base.Optional;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.logging.Logger;

import static com.codahale.dropwizard.testing.FixtureHelpers.fixture;
import static java.util.logging.Level.OFF;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestPersonResource extends ResourceTest {
    static {
        Logger.getLogger("com.sun.jersey").setLevel(OFF);
    }

    private static final String PERSON_ID = "9b89ec69-e844-4e88-ada6-5ee93e947a16";
    private static final String UNKNOWN_PERSON_ID = "8b89ec69-e844-4e88-ada6-5ee93e947a17";
    private static final String PERSON_PATH = "/people/" + PERSON_ID;

    private final Person person = Models.createTestPerson();
    private final PersonRepository repository = mock(PersonRepository.class);


    @Override
    protected void setUpResources() throws Exception {
        when(repository.findById(UUID.fromString(PERSON_ID)))
                .thenReturn(Optional.of(person));
        when(repository.findById(UUID.fromString(UNKNOWN_PERSON_ID)))
                .thenReturn(Optional.<Person>absent());

        addResource(new PersonResourceImpl(repository));
    }

    @Before
    public void setUpTestPerson() throws Exception {
        person.setId(UUID.fromString(PERSON_ID));
    }


    @Test
    public void getsPersonById() throws Exception {
        assertThat(client().resource(PERSON_PATH)
                .accept(APPLICATION_JSON)
                .get(Person.class))
                .isEqualTo(person);
    }

    @Test
    public void respondsWithCorrectJson() throws Exception {
        final String expectedJson = fixture("fixtures/person.json").replaceAll("\\s", "");

        assertThat(client().resource(PERSON_PATH)
                .accept(APPLICATION_JSON)
                .get(String.class))
                .isEqualTo(expectedJson);
    }

    @Test
    public void respondsWith404IfNoSuchUser() throws Exception {
        assertThat(client().resource("/people/" + UNKNOWN_PERSON_ID)
                .accept(APPLICATION_JSON)
                .get(ClientResponse.class)
                .getStatus())
                .isEqualTo(404);
    }
}
